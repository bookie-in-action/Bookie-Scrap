package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookFetcher;
import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookResponseDto;
import com.bookie.scrap.watcha.request.user.userwishbook.WatchaUserWishBookParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWishBookFetcherTest {

    @Autowired
    UserWishBookFetcher fetcher;

    @Test
    void fetch() throws JsonProcessingException {
        WatchaUserWishBookParam requestParam = new WatchaUserWishBookParam(1, 10);
        requestParam.setSortDirection();
        requestParam.setSortOption();

        UserWishBookResponseDto response = fetcher.fetch("2mwvggAE2vMa7", requestParam);
        log.debug(response.getMetaData().toString());
        log.debug(response.getResult().getNextUri().toString());
        log.debug(response.getResult().getUserWishBooks().toString());
    }
}