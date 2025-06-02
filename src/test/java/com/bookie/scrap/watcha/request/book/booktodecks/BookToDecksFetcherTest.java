package com.bookie.scrap.watcha.request.book.booktodecks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookToDecksFetcherTest {

    @Autowired
    BookToDecksFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        BookToDecksParam param = new BookToDecksParam(1, 10);

        BookToDecksResponseDto response = fetcher.fetch("byLKj8M", param);
        JsonNode jsonNode = response.getResult().getDecks().get(0);
        String code = response.getResult().getDeckCodes().get(0);


        log.debug(code);
        log.debug(jsonNode.toString());
    }
}