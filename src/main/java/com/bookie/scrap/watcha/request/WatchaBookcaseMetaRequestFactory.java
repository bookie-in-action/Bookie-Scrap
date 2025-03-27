package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.dto.WatchaBookcaseMetaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;


@Slf4j
public class WatchaBookcaseMetaRequestFactory implements WatchaRequestFactory<List<WatchaBookcaseMetaDto>> {

    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/decks?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final HttpClientResponseHandler<List<WatchaBookcaseMetaDto>> handler = WatchaBookcaseMetaResponseHandler.create();

    private final static WatchaBookcaseMetaRequestFactory INSTANCE = new WatchaBookcaseMetaRequestFactory();

    private WatchaBookcaseMetaRequestFactory() {
    }

    public static WatchaBookcaseMetaRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaBookcaseMetaDto>> createRequest(final String bookCode, final WatchaRequestParam requestParam) {
        Request<List<WatchaBookcaseMetaDto>> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = requestParam.buildUrlWithPageInfo(endPoint);

        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest bookcode: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }

    @Override
    public Request<List<WatchaBookcaseMetaDto>> createRequest(String bookCode) {
        Request<List<WatchaBookcaseMetaDto>> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest bookcode: {}, endpoint: {}, method: {}", bookCode, endPoint, HTTP_METHOD);

        return watchaRequest;
    }
}
