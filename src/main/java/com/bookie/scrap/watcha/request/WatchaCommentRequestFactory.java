package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaCommentDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaCommentRequestFactory implements WatchaRequestFactory<List<WatchaCommentDto>> {

    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/comments?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<List<WatchaCommentDto>> handler = WatchaCommentReponseHandler.create();

    private final static WatchaCommentRequestFactory INSTANCE = new WatchaCommentRequestFactory();

    private WatchaCommentRequestFactory() {}

    public static WatchaCommentRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaCommentDto>> createRequest(String value) {
        return null;
    }

    @Override
    public Request<List<WatchaCommentDto>> createRequest(final String bookCode, final PageInfo pageInfo) {

        WatchaRequestParam watchaParam = asWatchaParam(pageInfo);

        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = watchaParam.buildUrlWithParamInfo(endPoint);

        Request<List<WatchaCommentDto>> watchaRequest = new WatchaRequest<>();
        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }
}
