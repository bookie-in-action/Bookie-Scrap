package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
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
class UserInfoCollectionServiceTest {

    @Autowired
    private UserInfoCollectionService service;

    @Test
    void collect() throws Exception {
        service.collect("ZWpqMekrDqrkn", new WatchaBookCommentParam(null));
    }
}