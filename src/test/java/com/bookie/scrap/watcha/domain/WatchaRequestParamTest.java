package com.bookie.scrap.watcha.domain;

import com.bookie.legacy.watcha.domain.WatchaRequestParam;
import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

class WatchaRequestParamTest {

    @Test
    @DisplayName("클래스 생성을 위한 테스트 ")
    void URLBuilderClassTest() throws URISyntaxException {

        String resultUrl = new URIBuilder("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?")
                .addParameter("page", String.valueOf(1))
                .addParameter("size", String.valueOf(9))
                .build()
                .toString();

        System.out.println(resultUrl);
        Assertions.assertEquals("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?page=1&size=9", resultUrl);
    }

    @Test
    void testGetUrlWithPageInfo() {
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");
        String resultUrl = watchaRequestParam.buildUrlWithPageInfo("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?");

        Assertions.assertEquals("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?page=1&size=12", resultUrl);
    }

    @Test
    void testGetUrlWithParamInfo() {
        WatchaRequestParam watchaRequestParam = new WatchaRequestParam(1, 12, "", "");
        String resultUrl = watchaRequestParam.buildUrlWithParamInfo("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?");

        Assertions.assertEquals("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?page=1&size=12&order=popular&filter=all", resultUrl);
    }

}