package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.assertions.Assertions;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Import(SchedulerStubConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookMetaFetcherTest {

    @Autowired
    private BookMetaFetcher fetcher;

    @Test
    void fetch() {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 10);
        BookMetaResponseDto response = fetcher.fetch("byLKj8M", param);
        log.debug(response.getBookMeta().toString());
    }

    @Test
    @DisplayName("response의 content_Type이 book이 아닌 경우")
    void find_is_not_book() {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 1);
        BookMetaResponseDto response = fetcher.fetch("tEQ7A9P", param);
        log.info(response.getContentType());
        Assertions.assertFalse(response.isBook());
    }


    @Test
    @DisplayName("response의 content_Type이 book인 경우")
    void find_is_book() {
        WatchaBookMetaParam param = new WatchaBookMetaParam(1, 1);
        BookMetaResponseDto response = fetcher.fetch("byLKj8M", param);
        log.info(response.getContentType());
        Assertions.assertTrue(response.isBook());
    }
}