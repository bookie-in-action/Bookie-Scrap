package com.bookie.scrap.watcha.request.comment;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WatchaCommentFetcherTest {

    @Autowired
    private WatchaCommentFetcher fetcher;

    @Test
    void testExecute() {
        WatchaRequestParam requestParam = new WatchaRequestParam(1, 10);
        requestParam.setPopularOrder();

        fetcher.execute("byLKj8M", requestParam);
    }
}