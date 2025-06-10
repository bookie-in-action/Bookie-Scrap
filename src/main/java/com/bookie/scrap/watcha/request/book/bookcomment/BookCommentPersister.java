package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.domain.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BookCommentPersister implements WatchaPersistFactory<BookCommentResponseDto> {


    private final RedisStringListService userRedisService;
    private final BookCommentMongoRepository repository;

    public BookCommentPersister(
            @Qualifier("userCodeList") RedisStringListService userRedisService,
            BookCommentMongoRepository repository
    ) {
        this.userRedisService = userRedisService;
        this.repository = repository;
    }

    @Override
    public int persist(BookCommentResponseDto dto, String bookCode) throws JsonProcessingException {

        List<JsonNode> comments = dto.getResult().getComments();

        if (comments == null) {
            return 0;
        }

        log.debug("size: {}",comments.size());

        List<BookCommentDocument> documents = new ArrayList<>();
        List<String> userCodes = new ArrayList<>();
        for (int idx = 0; idx < comments.size(); idx++) {

            log.debug(
                    "comment idx: {}, value: {}",
                    idx,
                    JsonUtil.toPrettyJson(comments.get(idx))
            );
            log.debug("===========================");

            BookCommentDocument document = new BookCommentDocument();
            document.setBookCode(bookCode);
            document.setRawJson(JsonUtil.toMap(comments.get(idx)));
            documents.add(document);
            userCodes.add(comments.get(idx).get("user").get("code").asText());
        }

        repository.saveAll(documents);
        userRedisService.add(userCodes);
        return comments.size();

    }
}
