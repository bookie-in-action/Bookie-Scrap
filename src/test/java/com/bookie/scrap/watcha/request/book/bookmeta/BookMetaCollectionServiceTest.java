package com.bookie.scrap.watcha.request.book.bookmeta;

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
class BookMetaCollectionServiceTest {

    @Autowired
    BookMetaCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 10);
        service.collect("byLKj8M", param);
    }
}