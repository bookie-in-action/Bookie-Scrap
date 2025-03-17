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
    private int page;
    private int size;

    public WatchaBaseRequestParam(String bookCode, int page, int size) {
        validNotEmpty(bookCode, "BookCode parameter");
        validatePage(page);
        validateSize(size);

        this.bookCode = bookCode;
        this.page = page;
        this.size = size;
    }

    public void nextPage() {
        this.page += 1;
    }

    public String buildUrl(String baseUrl) {

        validNotEmpty(baseUrl, "BaseURL");

        try {
            String encodedBookCode = URLEncoder.encode(bookCode, StandardCharsets.UTF_8);

            return new URIBuilder(baseUrl)
                    .appendPathSegments(encodedBookCode, "items")
                    .addParameter("page", String.valueOf(page))
                    .addParameter("size", String.valueOf(size))
                    .build()
                    .toString();

        } catch (URISyntaxException e) {
            log.error("Failed to build URL for bookCode: {} with baseUrl: {}", bookCode, baseUrl, e);
            throw new IllegalStateException(e);
        }
    }

    private void validNotEmpty(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be null or empty");
        }
    }

    private void validatePage(int page) {
        if (page < 0) {
            throw new IllegalArgumentException("page parameter must be greater than or equal to 0");
        }
    }

    private void validateSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size parameter must be greater than or equal to 1");
        }
    }
}
