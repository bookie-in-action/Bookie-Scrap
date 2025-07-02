package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.common.util.JsonUtil;
import com.bookie.scrap.watcha.domain.WatchaPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class BookListPersister implements WatchaPersistor<BookListResponseDto> {

    private final RedisStringListService bookRedisService;

    private final BookListMongoRepository repository;

    public BookListPersister(
            @Qualifier("pendingBookCode") RedisStringListService bookRedisService,
            BookListMongoRepository repository
    ) {
        this.bookRedisService = bookRedisService;
        this.repository = repository;
    }

    @Override
    public int persist(BookListResponseDto dto, String deckCode) {

        List<JsonNode> books = dto.getResult().getBooks();

        if (books == null || books.isEmpty()) {
            return 0;
        }

        log.debug("size: {}",books.size());

        List<BookListDocument> documents = new ArrayList<>();

        int count = 0;
        for (int idx = 0; idx < books.size(); idx++) {
            try {
                BookListDocument document = new BookListDocument();
                document.setDeckCode(deckCode);
                document.setRawJson(JsonUtil.toMap(books.get(idx)));
                documents.add(document);

                log.debug(
                        "deckCode: {}, book idx: {}, value: {}",
                        deckCode,
                        idx,
                        JsonUtil.toPrettyJson(books.get(idx))
                );
                log.debug("===========================");

                count++;
            } catch (JsonProcessingException e) {
                log.warn("json 파싱 실패");
            }
        }

        repository.saveAll(documents);
        bookRedisService.add(dto.getResult().getBookCodes());

        return count;
    }
}
