package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

import com.bookie.scrap.watcha.request.user.userlikepeople.WatchaUserLikePeopleParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserLikeDeckFetcherTest {

    @Autowired
    private UserLikeDeckFetcher fetcher;


    @Test
    void fetch() throws JsonProcessingException {
        WatchaUserLikePeopleParam requestParam = new WatchaUserLikePeopleParam(1, 10);

        fetcher.fetch("2mwvggAE2vMa7", requestParam);
//        log.debug(result.getMetaData().toString());
//        log.debug(result.getResult().toString());
    }
}