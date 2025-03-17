package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.domain.RequestFactory;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;


@Slf4j
public class WatchaBookRequestFactory implements RequestFactory<WatchaBookDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/contents/%s";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<WatchaBookDto> handler = WatchaBookReponseHandler.create();

    private final static WatchaBookRequestFactory INSTANCE = new WatchaBookRequestFactory();

    private WatchaBookRequestFactory() {}

    public static WatchaBookRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookDto> createRequest(final String value) {
        Request<WatchaBookDto> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

}
