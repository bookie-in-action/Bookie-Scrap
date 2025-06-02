package com.bookie.scrap.watcha.request.deck;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeckFetcherTest {

    @Autowired
    private DeckFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaDeckParam param = new WatchaDeckParam(1, 10);
        DeckResponseDto response = fetcher.fetch("gcdkyKnXjN", param);

        log.debug(response.getMetaData().toString());
        log.debug(response.getResult().getBookCodes().toString());
        log.debug(response.getResult().getBooks().get(0).toString());

    }
}