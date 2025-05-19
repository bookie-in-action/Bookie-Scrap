package com.bookie.legacy.watcha.request;

import com.bookie.legacy.common.domain.Request;
import com.bookie.legacy.common.domain.RequestFactory;
import com.bookie.legacy.common.lifecycle.InitManager;
import com.bookie.legacy.watcha.dto.WatchaBookMetaDto;
import com.bookie.legacy.watcha.request.WatchaBookMetaRequestFactory;
import com.bookie.legacy.watcha.type.WatchaExternalService;
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
        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", bookDetail.getUrlMap().get(WatchaExternalService.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", bookDetail.getUrlMap().get(WatchaExternalService.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", bookDetail.getUrlMap().get(WatchaExternalService.KYOBO));
//boBNGJd
    }

    @Test
    void test() {
        RequestFactory<WatchaBookMetaDto> watchaBookRequestFactory = WatchaBookMetaRequestFactory.getInstance();

        Request<WatchaBookMetaDto> watchaRequest = watchaBookRequestFactory.createRequest("boBNGJd");
        WatchaBookMetaDto bookDetail = watchaRequest.execute();
        String hd = bookDetail.getBookPoster().getHd();

        System.out.println("=============hd" + hd);

    }
}

