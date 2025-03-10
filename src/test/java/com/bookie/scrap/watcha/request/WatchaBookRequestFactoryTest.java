package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import com.bookie.scrap.watcha.type.WatchaBookType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class WatchaBookRequestFactoryTest {
    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

    }

    @Test
    void createRequest() {
        RequestFactory<WatchaBookDto> watchaBookRequestFactory = WatchaBookRequestFactory.getInstance();

        Request<WatchaBookDto> watchaRequest = watchaBookRequestFactory.createRequest("byLKj8M");
        WatchaBookDto bookDetail = watchaRequest.execute();
        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", bookDetail.getUrlMap().get(WatchaBookType.EXTERNAL_SERVICE.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", bookDetail.getUrlMap().get(WatchaBookType.EXTERNAL_SERVICE.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", bookDetail.getUrlMap().get(WatchaBookType.EXTERNAL_SERVICE.KYOBO));

    }
}

