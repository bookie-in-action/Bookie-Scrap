package com.bookie.scrap.config;

import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.config.WatchaComment;
import com.bookie.scrap.watcha.config.WatchaDeck;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.response.WatchaBookDetail;
import com.bookie.scrap.watcha.response.WatchaCommentDetail;
import com.bookie.scrap.watcha.type.WatchaBookType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

@Slf4j
class BaseRequestTest {

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
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBook("byLKj8M"));
        Assertions.assertEquals(WatchaBookType.EXTERNAL_SERVICE.values().length, book.getUrlMap().size());
    }

    @Test
    public void contentTest() { // 하나의 책에 대해 모든 코멘트를 가져오기
        System.out.println("================ contentTest Execute ==================");
        HttpRequestExecutor.execute(new WatchaComment("byLKj8M", 1, 10));
    }


    @Test
    public void deckTest() {
        HttpRequestExecutor.execute(new WatchaDeck("byLKj8M", 1, 10));
    }



}