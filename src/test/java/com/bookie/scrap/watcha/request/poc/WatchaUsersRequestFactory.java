package com.bookie.scrap.watcha.request.poc;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookMetaDto;
import com.bookie.scrap.watcha.request.WatchaBookMetaReponseHandler;
import com.bookie.scrap.watcha.request.WatchaRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;


@Slf4j
public class WatchaUsersRequestFactory implements WatchaRequestFactory<WatchaBookMetaDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/users/%s";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final HttpClientResponseHandler<WatchaBookMetaDto> handler = WatchaBookMetaReponseHandler.create();

    private final static WatchaUsersRequestFactory INSTANCE = new WatchaUsersRequestFactory();

    private WatchaUsersRequestFactory() {}

    public static WatchaUsersRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookMetaDto> createRequest(final String value) {

        Request<WatchaBookMetaDto> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

}
