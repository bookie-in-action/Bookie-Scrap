package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.http.HttpMethod;
import com.bookie.scrap.watcha.domain.WatchaRequestFactory;
import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.bookie.scrap.watcha.dto.WatchaBookcaseDTO;
import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.util.List;

@Slf4j
public class WatchaCommentRequestFactory implements WatchaRequestFactory<List<WatchaCommentDetailDTO>> {

    // https://pedia.watcha.com/api/contents/byLKj8M/comments?filter=all&order=popular&page=2&size=9
    private final String HTTP_URL_PATTERN = "https://pedia.watcha.com/api/contents/%s/comments?";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    HttpClientResponseHandler<List<WatchaCommentDetailDTO>> handler = WatchaCommentReponseHandler.create();

    private final static WatchaCommentRequestFactory INSTANCE = new WatchaCommentRequestFactory();

    private WatchaCommentRequestFactory() {}

    public static WatchaCommentRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<List<WatchaCommentDetailDTO>> createRequest(String value) {
        return null;
    }

    @Override
    public Request<List<WatchaCommentDetailDTO>> createRequest(final String bookCode, final WatchaRequestParam requestParam) {
        Request<List<WatchaCommentDetailDTO>> watchaRequest = new WatchaRequest<>();

        String endPoint = String.format(HTTP_URL_PATTERN, bookCode);
        String endPointWithParam = requestParam.buildUrlWithParamInfo(endPoint);

        watchaRequest.setMainRequest(HTTP_METHOD, endPointWithParam);
        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", bookCode, endPointWithParam, HTTP_METHOD);

        return watchaRequest;
    }
}
