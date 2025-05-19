package com.bookie.legacy.watcha.request.poc;

import com.bookie.legacy.common.domain.Request;
import com.bookie.legacy.common.http.HttpMethod;
import com.bookie.legacy.watcha.domain.WatchaRequestFactory;
import com.bookie.legacy.watcha.dto.WatchaBookMetaDto;
import com.bookie.legacy.watcha.request.WatchaBookMetaReponseHandler;
import com.bookie.legacy.watcha.request.WatchaRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;


@Slf4j
public class WatchaUserWishesBookRequestFactory implements WatchaRequestFactory<WatchaBookMetaDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/users/%s/contents/books/wishes?order=recent";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final HttpClientResponseHandler<WatchaBookMetaDto> handler = WatchaBookMetaReponseHandler.create();

    private final static WatchaUserWishesBookRequestFactory INSTANCE = new WatchaUserWishesBookRequestFactory();

    private WatchaUserWishesBookRequestFactory() {}

    public static WatchaUserWishesBookRequestFactory getInstance() {
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
