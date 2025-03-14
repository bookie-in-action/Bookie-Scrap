package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.request.RequestFactory;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.domain.WatchaBaseRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaBookcaseRequestFactory implements RequestFactory<List<WatchaBookcaseDTO>> {

    // COMMENT  : /api/contents/gcdkyKnXjN/comments?page=1&size=12;
    // DECK     : /api/decks/gcdkyKnXjN/items?page=1&size=12
    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%d/items?page=%s&size=%s";
    private final String HTTP_BASE_URL = "https://pedia.watcha.com/api/decks";

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
    public Request<List<WatchaBookcaseDTO>> createRequest(WatchaBaseRequestParam watchaRequestParamDTO) {
        Request<List<WatchaBookcaseDTO>> watchaRequest = new WatchaRequest<>();

        String endPoint = watchaRequestParamDTO.buildUrl(HTTP_BASE_URL);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", watchaRequestParamDTO.getBookCode(), endPoint, HTTP_METHOD);

        return watchaRequest;
    }
}
