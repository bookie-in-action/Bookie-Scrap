package com.bookie.scrap.watcha.request.deck.boolist;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
import com.bookie.scrap.watcha.request.deck.booklist.BookListFetcher;
import com.bookie.scrap.watcha.request.deck.booklist.BookListResponseDto;
import com.bookie.scrap.watcha.request.deck.booklist.WatchaBookListParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
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
class BookListFetcherTest {

    @Autowired
    private BookListFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaBookListParam param = new WatchaBookListParam(1, 10);
        BookListResponseDto response = fetcher.fetch("gcdkyKnXjN", param);

        log.debug(response.getMetaData().toString());
        log.debug(response.getResult().getBookCodes().toString());
        log.debug(response.getResult().getBooks().get(0).toString());

    }
}