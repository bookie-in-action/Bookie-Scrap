package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookListPersister implements WatchaPersistor<BookListResponseDto> {

    private final BookListMongoRepository repository;

    @Override
    public int persist(BookListResponseDto dto, String deckCode) {

        List<JsonNode> books = dto.getResult().getBooks();

        if (books == null || books.isEmpty()) {
            return 0;
        }

        log.info("BookList size: {}",books.size());

        List<BookListDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < books.size(); idx++) {
            try {
                BookListDocument document = new BookListDocument();
                document.setDeckCode(deckCode);
                document.setRawJson(JsonUtil.toMap(books.get(idx)));
                documents.add(document);

                log.info("deckCode: {} book idx: {} saved", deckCode, count);
                log.debug(
                        "deckCode: {}, book idx: {}, value: {}",
                        deckCode,
                        idx,
                        JsonUtil.toPrettyJson(books.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.error("json 파싱 실패");
            }
        }

        repository.saveAll(documents);

        return count;
    }
}
