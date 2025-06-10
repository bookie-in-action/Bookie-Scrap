package com.bookie.scrap.watcha.request.deck.boolist;

import com.bookie.scrap.watcha.request.deck.booklist.DeckCollectionService;
import com.bookie.scrap.watcha.request.deck.booklist.WatchaDeckParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeckCollectionServiceTest {

    @Autowired
    DeckCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaDeckParam param = new WatchaDeckParam(1, 10);
        service.collect("gcdkyKnXjN", param);
    }
}