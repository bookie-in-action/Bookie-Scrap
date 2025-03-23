package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.domain.RequestFactory;
import com.bookie.scrap.common.lifecycle.InitManager;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.type.WatchaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WatchaBookMetaRequestFactoryTest {

    @BeforeAll
    public static void init() {
        InitManager initManager = new InitManager();
        initManager.devInit();
    }

    @Test
    void createRequest() {
        RequestFactory<WatchaBookMetaDto> watchaBookRequestFactory = WatchaBookMetaRequestFactory.getInstance();

        Request<WatchaBookMetaDto> watchaRequest = watchaBookRequestFactory.createRequest("byLKj8M");
        WatchaBookMetaDto bookDetail = watchaRequest.execute();
        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", bookDetail.getUrlMap().get(WatchaType.EXTERNAL_SERVICE.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", bookDetail.getUrlMap().get(WatchaType.EXTERNAL_SERVICE.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", bookDetail.getUrlMap().get(WatchaType.EXTERNAL_SERVICE.KYOBO));

    }
}

