package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentCollectionService;
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
class BookCommentCollectionServiceTest {

    @Autowired
    private BookCommentCollectionService service;

    @Test
    void testService() throws Exception {
        WatchaBookCommentParam requestParam = new WatchaBookCommentParam(1, 10);
        requestParam.setPopularOrder();
        service.collect("byLKj8M", requestParam);
    }
}