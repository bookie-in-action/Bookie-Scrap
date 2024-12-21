package com.bookie.scrap.config;

import com.bookie.scrap.config.watcha.WatchaBookConfig;
import com.bookie.scrap.config.watcha.WatchaCommentConfig;
import com.bookie.scrap.config.watcha.WatchaDeckConfig;
import com.bookie.scrap.config.watcha.WatchaExternalUrlConfig;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class BaseRequestConfigTest {

    @Test
    public void externalServiceLinkBookTest() {
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));
        Assertions.assertEquals(3, book.getExternalUrls().size());
    }

    @Test
    public void contentTest() {
        HttpRequestExecutor.execute(new WatchaCommentConfig("byLKj8M", 1, 10));
    }


    @Test
    public void deckTest() {
        HttpRequestExecutor.execute(new WatchaDeckConfig("byLKj8M", 1, 10));
    }

    @Test
    public void externalTest() {
        String response = HttpRequestExecutor.execute(new WatchaExternalUrlConfig("aHR0cHM6Ly93d3cueWVzMjQuY29tL1Byb2R1Y3QvR29vZHMvMTA1NTI2MDQ3"));
    }

}