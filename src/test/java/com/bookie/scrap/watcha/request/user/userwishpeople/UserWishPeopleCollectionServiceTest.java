package com.bookie.scrap.watcha.request.user.userwishpeople;

import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserWishPeopleCollectionServiceTest {

    @Autowired
    private UserWishPeopleCollectionService service;

    @Test
    void collect() throws Exception {
        WatchaBookCommentParam requestParam = new WatchaBookCommentParam(1, 10);

        service.collect("JgAx8wnyY5LbO", requestParam);
    }
}