package com.bookie.legacy.common.domain;

import com.bookie.legacy.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;


@Slf4j
public class PageInfo {

    protected int page;
    protected int size;

    public PageInfo(int page, int size) {
        validatePage(page);
        validateSize(size);

        this.page = page;
        this.size = size;
    }

    public void nextPage() {
        this.page += 1;
    }

    public String buildUrlWithPageInfo(String baseUrl) {

        validNotEmpty(baseUrl, "baseUrl");

        try {
            return new URIBuilder(baseUrl)
                    .addParameter("page", String.valueOf(page))
                    .addParameter("size", String.valueOf(size))
                    .build()
                    .toString();

        } catch (URISyntaxException e) {
            log.error("Failed to build URL for baseUrl {} with PageInfo: {}", baseUrl, this);
            throw new IllegalStateException(e);
        }
    }

    protected void validNotEmpty(String text, String fieldName) {
        if (!StringUtil.hasText(text)) {
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

