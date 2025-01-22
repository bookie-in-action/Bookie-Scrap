package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.Request;
import com.bookie.scrap.common.RequestFactory;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.util.ResponseHandlerMaker;
import com.bookie.scrap.watcha.response.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WatchaBookRequestFactory implements RequestFactory<WatchaBookDetail> {
    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/contents/%s";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final static WatchaBookRequestFactory INSTANCE = new WatchaBookRequestFactory();

    private WatchaBookRequestFactory() {}

    public static WatchaBookRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookDetail> createRequest(final String value) {
        Request<WatchaBookDetail> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(
                ResponseHandlerMaker.getWatchaHandlerTemplate(WatchaBookReponseHandler.getHandlerLogic())
        );

        log.debug("Created WatchaRequest for value: {}, endpoint: {}", value, endPoint);

        return watchaRequest;
    }

}
