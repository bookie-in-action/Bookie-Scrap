package com.bookie.scrap.config.watcha;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class WatchaBookConfigTest {

    @Test
    public void externalServiceLinkTest() {
        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));

        Assertions.assertEquals("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=284657330", book.getExternalUrls().get(WatchaBookDetail.TYPE.ALADIN));
        Assertions.assertEquals("https://www.yes24.com/Product/Goods/105526047", book.getExternalUrls().get(WatchaBookDetail.TYPE.YES24));
        Assertions.assertEquals("https://product.kyobobook.co.kr/detail/S000001925800", book.getExternalUrls().get(WatchaBookDetail.TYPE.KYOBO));
    }


}