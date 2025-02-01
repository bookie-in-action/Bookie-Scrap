package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.watcha.dto.WatchaBookDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;


@Slf4j
public class WatchaBookRequestFactory implements RequestFactory<WatchaBookDTO> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/contents/%s";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<WatchaBookDTO> handler = WatchaBookReponseHandler.create();

    private final static WatchaBookRequestFactory INSTANCE = new WatchaBookRequestFactory();

    private WatchaBookRequestFactory() {}

    public static WatchaBookRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookDTO> createRequest(final String value) {
        Request<WatchaBookDTO> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

}
