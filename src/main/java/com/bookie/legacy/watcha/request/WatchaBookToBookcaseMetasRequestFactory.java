package com.bookie.legacy.watcha.request;

import com.bookie.legacy.common.domain.PageInfo;
import com.bookie.legacy.common.domain.Request;
import com.bookie.legacy.common.http.HttpMethod;
import com.bookie.legacy.common.util.ThreadUtil;
import com.bookie.legacy.watcha.domain.WatchaRequestFactory;
import com.bookie.legacy.watcha.dto.WatchaBookcaseMetaDto;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class WatchaBookToBookcaseMetasRequestFactory implements WatchaRequestFactory<List<WatchaBookcaseMetaDto>> {

    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/decks?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    private final static WatchaBookToBookcaseMetasRequestFactory INSTANCE = new WatchaBookToBookcaseMetasRequestFactory();

    private WatchaBookToBookcaseMetasRequestFactory() {
    }

    public static WatchaBookToBookcaseMetasRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaBookcaseMetaDto>> createRequest(final String bookCode, final PageInfo pageInfo) {

        Request<List<WatchaBookcaseMetaDto>> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = pageInfo.buildUrlWithPageInfo(endPoint);

        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(WatchaBookToBookcaseMetasResponseHandler.create(bookCode));

        log.debug("Created WatchaRequest bookcode: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }

    @Override
    public Request<List<WatchaBookcaseMetaDto>> createRequest(String bookCode) {
        ThreadUtil.sleep();

        Request<List<WatchaBookcaseMetaDto>> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(WatchaBookToBookcaseMetasResponseHandler.create(bookCode));

        log.debug("Created WatchaRequest bookcode: {}, endpoint: {}, method: {}", bookCode, endPoint, HTTP_METHOD);

        return watchaRequest;
    }
}
