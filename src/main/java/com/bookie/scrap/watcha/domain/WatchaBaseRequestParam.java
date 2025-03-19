package com.bookie.scrap.watcha.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Getter
@Setter
public class WatchaBaseRequestParam {

    private String bookCode;
    private String page;
    private String size;

    public WatchaBaseRequestParam(String bookCode, String page, String size) {
        this.bookCode = bookCode;
        this.page = page;
        this.size = size;
    }

    public String buildUrl(String baseUrl) {
        // 필수 값 검증
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Base URL must not be null or empty.");
        }
        if (bookCode == null || bookCode.isBlank()) {
            throw new IllegalArgumentException("Book Code parameter must not be null or empty.");
        }
        if (page == null || page.isBlank()) {
            throw new IllegalArgumentException("Page parameter must not be null or empty.");
        }
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Size parameter must not be null or empty.");
        }

        try {
            return new URIBuilder(baseUrl + "/" + URLEncoder.encode(bookCode, StandardCharsets.UTF_8))
                    .addParameter("page", page)
                    .addParameter("size", size)
                    .build()
                    .toString();
        } catch (URISyntaxException e) {
            log.error("Invalid URL : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
