package com.bookie.scrap.watcha.domain;

import org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
class WatchaBaseRequestParamTest {

    @Test
    void buildUrl() throws URISyntaxException {

        String encodedBookCode = URLEncoder.encode("gcdkyKnXjN", StandardCharsets.UTF_8);

        String resultUrl = new URIBuilder("https://pedia.watcha.com/api/decks")
                .appendPathSegments(encodedBookCode, "items")
                .addParameter("page", String.valueOf(1))
                .addParameter("size", String.valueOf(9))
                .build()
                .toString();

        System.out.println(resultUrl);
        Assertions.assertEquals("https://pedia.watcha.com/api/decks/gcdkyKnXjN/items?page=1&size=9", resultUrl);
    }
}