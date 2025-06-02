package com.bookie.scrap.watcha.request.userwishpeople;

import com.bookie.scrap.watcha.request.bookcomment.WatchaBookCommentParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWishPeopleFetcherTest {
    @Autowired
    private UserWishPeopleFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaBookCommentParam requestParam = new WatchaBookCommentParam(1, 10);

        UserWishPeopleResponseDto result = fetcher.fetch("ZWpqMekrDqrkn", requestParam);
        log.debug(result.getMetaData().toString());
        log.debug(result.getResult().toString());
    }
}