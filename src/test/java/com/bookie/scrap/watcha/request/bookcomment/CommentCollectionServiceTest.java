package com.bookie.scrap.watcha.request.bookcomment;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentCollectionServiceTest {

    @Autowired
    private BookCommentCollectionService service;

    @Test
    void testService() {
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 10);
        requestParam.setPopularOrder();
        service.collect("byLKj8M", requestParam);
    }
}