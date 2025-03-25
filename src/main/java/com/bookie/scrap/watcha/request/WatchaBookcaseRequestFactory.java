package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaBookcaseRequestFactory implements WatchaRequestFactory<List<WatchaBookcaseDTO>> {

    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%s/items?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<List<WatchaBookcaseDTO>> handler = WatchaBookcaseReponseHandler.create();

    private final static WatchaBookcaseRequestFactory INSTANCE = new WatchaBookcaseRequestFactory();

    public static WatchaBookcaseRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaBookcaseDTO>> createRequest(String value) {
        return null;
    }

    @Override
    public Request<List<WatchaBookcaseDTO>> createRequest(String bookCode, WatchaRequestParam requestParam) {
        Request<List<WatchaBookcaseDTO>> watchaRequest = new WatchaRequest<>();

        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = requestParam.buildUrlWithPageInfo(endPoint);

        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }
}
