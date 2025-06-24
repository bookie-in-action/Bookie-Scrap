package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.scheduler.SchedulerStubConfig;
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
class DeckInfoFetcherTest {

    @Autowired
    private DeckInfoFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaBookListParam param = new WatchaBookListParam(1, 10);
        DeckInfoResponseDto dto = fetcher.fetch("gcdkyKnXjN", param);



    }
}