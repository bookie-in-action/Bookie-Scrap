package com.bookie.scrap.watcha.domain;

import com.bookie.scrap.common.domain.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

@Slf4j
public class WatchaPageInfo extends PageInfo {

    public WatchaPageInfo(Void noUse) {
        super(1, 1);
    }

    public WatchaPageInfo(int page, int size) {
        super(page, size);
    }

    public String buildUrlWithParamInfo(String baseUrl) {
        super.validNotEmpty(baseUrl, "baseUrl");

        try {
            return new URIBuilder(baseUrl)
                    .addParameter("page", String.valueOf(super.page))
                    .addParameter("size", String.valueOf(super.size))
                    .build()
                    .toString();

        } catch (URISyntaxException e) {
            log.error("Failed to build URL for baseUrl {}", baseUrl);
            throw new IllegalStateException(e);
        }
    }

}
