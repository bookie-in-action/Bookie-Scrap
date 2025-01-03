package com.bookie.scrap.config.watcha;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class WatchaBookConfigTest {

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
    public void externalServiceLinkTest() {
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBook("byLKj8M"));

        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", book.getUrlMap().get(WatchaBookDetail.TYPE.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", book.getUrlMap().get(WatchaBookDetail.TYPE.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", book.getUrlMap().get(WatchaBookDetail.TYPE.KYOBO));

    }


}