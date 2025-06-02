package com.bookie.scrap.watcha.request.deck;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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