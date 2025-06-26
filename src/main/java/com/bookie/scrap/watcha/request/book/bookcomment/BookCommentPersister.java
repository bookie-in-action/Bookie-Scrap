package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
            @Qualifier("pendingUserCode") RedisStringListService userRedisService,
            BookCommentMongoRepository repository
    ) {
        this.userRedisService = userRedisService;
        this.repository = repository;
    }

    @Override
    public int persist(BookCommentResponseDto dto, String bookCode) {

        List<JsonNode> comments = dto.getResult().getComments();

        if (comments == null || comments.isEmpty()) {
            return 0;
        }

        log.debug("size: {}",comments.size());

        List<BookCommentDocument> documents = new ArrayList<>();
        List<String> userCodes = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < comments.size(); idx++) {

            try {
                BookCommentDocument document = new BookCommentDocument();
                document.setBookCode(bookCode);
                document.setRawJson(JsonUtil.toMap(comments.get(idx)));
                documents.add(document);

                log.debug(
                        "comment idx: {}, value: {}",
                        idx,
                        JsonUtil.toPrettyJson(comments.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.warn("json 파싱 실패");
            }
        }

        repository.saveAll(documents);
        userRedisService.add(dto.getResult().getUserCodes());
        return count;

    }
}
