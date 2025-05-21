package com.bookie.scrap.watcha;

import com.bookie.scrap.common.LegacyHttpMethodType;
import com.bookie.scrap.common.Request;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class WatchaBookMetaHttpFactory implements WatchaHttpFactory<WatchaBookMetaDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/contents/%s";
    private final LegacyHttpMethodType HTTP_METHOD = LegacyHttpMethodType.GET;

    @Override
    public Request createRequest(final String value) {

        Request watchaRequest = new WatchaRequest();
        String endPoint = String.format(HTTP_URL_PATTERN, value);
        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

    private final HttpClientResponseHandler<WatchaBookMetaDto> handler = WatchaBookMetaReponseHandler.create();

    @Override
    public WatchaBookMetaDto createResponse() {

        return null;
    }

}
