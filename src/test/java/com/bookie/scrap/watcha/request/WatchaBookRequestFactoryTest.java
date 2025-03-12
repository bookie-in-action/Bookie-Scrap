package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.common.startup.Initializer;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import com.bookie.scrap.watcha.type.WatchaBookType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WatchaBookRequestFactoryTest {

    @BeforeAll
    public static void init() {
        Initializer initializer = new Initializer();
        initializer.devInit();
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

