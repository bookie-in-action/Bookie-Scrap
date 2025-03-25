package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.domain.RequestFactory;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaBookcaseRequestFactory implements WatchaRequestFactory<List<WatchaBookcaseDTO>> {

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
    public Request<List<WatchaBookcaseDTO>> createRequest(WatchaRequestParam requestParam) {
        Request<List<WatchaBookcaseDTO>> watchaRequest = new WatchaRequest<>();

        String endPoint = requestParam.buildUrlWithParamInfo(HTTP_BASE_URL);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

//        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", watchaWatchaRequestParamDTO.getBookCode(), endPoint, HTTP_METHOD);

        return watchaRequest;
    }
}
