package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookCommentPersister implements WatchaPersistFactory<BookCommentResponseDto> {

    private final ObjectMapper mapper;
    private final BookCommentMongoRepository repository;

    @Override
    public void persist(BookCommentResponseDto dto, String bookCode) throws JsonProcessingException {

        List<JsonNode> comments = dto.getResult().getComments();

        if (comments == null) {
            return;
        }

        log.debug("size: {}",comments.size());

        for (int idx = 0; idx < comments.size(); idx++) {

            log.debug(
                    "comment idx: {}, value: {}",
                    idx,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(comments.get(idx))
            );
            log.debug("===========================");

            BookCommentDocument document = new BookCommentDocument();
            document.setBookCode(bookCode);
            document.setRawJson(comments.get(idx).toString());
            repository.save(document);
        }

    }
}
