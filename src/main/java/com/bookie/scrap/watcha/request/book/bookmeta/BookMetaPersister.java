package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookMetaPersister implements WatchaPersistFactory<BookMetaResponseDto> {

    private final BookMetaMongoRepository repository;

    @Override
    public void persist(BookMetaResponseDto dto, String bookCode) throws JsonProcessingException {

        JsonNode bookMeta = dto.getBookMeta();

        log.debug(
                "bookCode: {}, value: {}",
                bookCode,
                JsonUtil.toPrettyJson(bookMeta)
        );
        log.debug("===========================");


        BookMetaDocument document = new BookMetaDocument();
        document.setBookCode(bookCode);
        document.setRawJson(JsonUtil.toMap(bookMeta));
        repository.save(document);



    }
}
