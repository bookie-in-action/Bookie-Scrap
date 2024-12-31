package com.bookie.scrap.config.watcha;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WatchaBookConfigTest {

    @Test
    public void externalServiceLinkTest() {
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));

        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", book.getExternalUrls().get(WatchaBookDetail.TYPE.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", book.getExternalUrls().get(WatchaBookDetail.TYPE.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", book.getExternalUrls().get(WatchaBookDetail.TYPE.KYOBO));
    }

    @Test
    public void getRedirectUrlTest() {
        WatchaBookConfig watchaBookConfig = new WatchaBookConfig("");
        String aladinUrl = watchaBookConfig.fetchWatchaRedirectUrl("https://redirect.watcha.com/galaxy/aHR0cDovL3d3dy5hbGFkaW4uY28ua3Ivc2hvcC93cHJvZHVjdC5hc3B4P0l0ZW1JZD0yODQ2NTczMzAmcGFydG5lcj13YXRjaGE");
        Assertions.assertEquals("http://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330&partner=watcha", aladinUrl);
    }

    @Test
    public void getAladinUrl() {
        WatchaBookConfig watchaBookConfig = new WatchaBookConfig("");

        //www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156

        String aladinUrl = watchaBookConfig.fetchWatchaRedirectUrl("https://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791189327156");
    }

}