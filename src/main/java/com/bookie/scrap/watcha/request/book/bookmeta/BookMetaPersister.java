package com.bookie.scrap.watcha.request.book.bookmeta;

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
public class BookMetaPersister implements WatchaPersistFactory<BookMetaResponseDto> {

    private final ObjectMapper mapper;
    private final BookMetaMongoRepository repository;

    @Override
    public void persist(BookMetaResponseDto dto, String bookCode) throws JsonProcessingException {

        JsonNode bookMeta = dto.getBookMeta();

        log.debug(
                "bookCode: {}, value: {}",
                bookCode,
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookMeta)
        );
        log.debug("===========================");

        BookMetaDocument document = new BookMetaDocument();
        document.setBookCode(bookCode);
        document.setRawJson(bookMeta);
        repository.save(document);

    }
}
