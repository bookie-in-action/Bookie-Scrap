package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

import com.bookie.scrap.common.domain.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

@Slf4j
public class WatchaUserLikeDeckParam extends PageInfo {

    public WatchaUserLikeDeckParam(int page, int size) {
        super(page, size);
    }


    @Override
    public String buildUrlWithParamInfo(String baseUrl) {
        super.validNotEmpty(baseUrl, "baseUrl");

        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl)
                    .addParameter("page", String.valueOf(super.page))
                    .addParameter("size", String.valueOf(super.size));


            return uriBuilder.build().toString();

        } catch (URISyntaxException e) {
            log.error("Failed to build URL for baseUrl {}", baseUrl);
            throw new IllegalStateException(e);
        }
    }

}
