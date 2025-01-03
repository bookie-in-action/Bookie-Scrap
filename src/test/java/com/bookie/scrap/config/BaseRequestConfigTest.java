package com.bookie.scrap.config;

import com.bookie.scrap.config.watcha.WatchaBookConfig;
import com.bookie.scrap.config.watcha.WatchaCommentConfig;
import com.bookie.scrap.config.watcha.WatchaDeckConfig;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
class BaseRequestConfigTest {

    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.stream().forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

    }

    @Test
    public void externalServiceLinkBookTest() {
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));
        Assertions.assertEquals(WatchaBookDetail.TYPE.values().length, book.getUrlMap().size());
    }

    @Test
    public void contentTest() {
        HttpRequestExecutor.execute(new WatchaCommentConfig("byLKj8M", 1, 10));
    }


    @Test
    public void deckTest() {
        HttpRequestExecutor.execute(new WatchaDeckConfig("byLKj8M", 1, 10));
    }



}