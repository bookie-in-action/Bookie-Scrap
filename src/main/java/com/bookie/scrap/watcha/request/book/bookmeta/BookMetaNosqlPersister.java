package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookMetaNosqlPersister implements WatchaPersistFactory<BookMetaResponseDto> {

    private final BookMetaMongoRepository repository;

    @Override
    public int persist(BookMetaResponseDto dto, String bookCode) {

        JsonNode bookMeta = dto.getBookMeta();

        if (bookMeta == null) {
            return 0;
        }

        BookMetaDocument document = new BookMetaDocument();

        try {
            document.setBookCode(bookCode);
            document.setRawJson(JsonUtil.toMap(bookMeta));
            repository.save(document);

            log.debug(
                    "bookCode: {}, value: {}",
                    bookCode,
                    JsonUtil.toPrettyJson(bookMeta)
            );
            log.debug("===========================");

            return 1;
        } catch (JsonProcessingException e) {
            log.warn("json 파싱 실패");
            return 0;
        }

    }
}
