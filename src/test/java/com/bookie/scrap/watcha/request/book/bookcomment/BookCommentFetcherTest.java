package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentFetcher;
import com.bookie.scrap.watcha.request.book.bookcomment.BookCommentResponseDto;
import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookCommentFetcherTest {

    @Autowired
    private BookCommentFetcher fetcher;


    @Test
    void testExecute() throws Exception {
        WatchaBookCommentParam requestParam = new WatchaBookCommentParam(1, 10);
        requestParam.setPopularOrder();

        BookCommentResponseDto response = fetcher.fetch("byLKj8M", requestParam);

        assertEquals(10, response.getResult().getComments().size());

        log.debug(response.getMetaData().toString());
    }

}
