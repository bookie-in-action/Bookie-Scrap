package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.watcha.request.book.bookmeta.rdb.WatchaBookMetaParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookMetaFetcherTest {

    @Autowired
    private BookMetaFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 10);
        BookMetaResponseDto response = fetcher.fetch("byLKj8M", param);
        log.debug(response.getBookMeta().toString());
    }
}