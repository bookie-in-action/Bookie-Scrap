package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.common.util.ThreadUtil;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookcaseToBookDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaBookcaseToBooksRequestFactory implements WatchaRequestFactory<List<WatchaBookcaseToBookDto>> {

    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/decks/%s/items?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<List<WatchaBookcaseToBookDto>> handler = WatchaBookcaseReponseHandler.create();

    private final static WatchaBookcaseToBooksRequestFactory INSTANCE = new WatchaBookcaseToBooksRequestFactory();

    public static WatchaBookcaseToBooksRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaBookcaseToBookDto>> createRequest(String value) {
        return null;
    }

    @Override
    public Request<List<WatchaBookcaseToBookDto>> createRequest(String bookCode, PageInfo pageInfo) {
        ThreadUtil.sleep();
        Request<List<WatchaBookcaseToBookDto>> watchaRequest = new WatchaRequest<>();

        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = pageInfo.buildUrlWithPageInfo(endPoint);

        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }
}
