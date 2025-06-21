package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.watcha.request.deck.booklist.BooListCollectionService;
import com.bookie.scrap.watcha.request.deck.booklist.WatchaBookListParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Profile("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeckInfoCollectionServiceTest {
    @Autowired
    DeckInfoCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaBookListParam param = new WatchaBookListParam(1, 10);
        service.collect("gcdkyKnXjN", param);
    }
}