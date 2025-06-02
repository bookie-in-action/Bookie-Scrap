package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.watcha.request.user.userwishbook.UserWishBookCollectionService;
import com.bookie.scrap.watcha.request.user.userwishbook.WatchaUserWishBookParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWishBookCollectionServiceTest {

    @Autowired
    private UserWishBookCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaUserWishBookParam requestParam = new WatchaUserWishBookParam(1, 10);
        requestParam.setSortDirection();
        requestParam.setSortOption();

        service.collect("2mwvggAE2vMa7", requestParam);
    }
}