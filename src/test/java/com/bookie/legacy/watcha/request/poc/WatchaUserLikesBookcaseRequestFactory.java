package com.bookie.legacy.watcha.request.poc;

import com.bookie.legacy.common.domain.Request;
import com.bookie.legacy.common.http.HttpMethod;
import com.bookie.legacy.watcha.domain.WatchaRequestFactory;
import com.bookie.legacy.watcha.dto.WatchaBookMetaDto;
import com.bookie.legacy.watcha.request.WatchaBookMetaReponseHandler;
import com.bookie.legacy.watcha.request.WatchaRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.message.BasicHeader;

import java.time.Instant;


@Slf4j
public class WatchaUserLikesBookcaseRequestFactory implements WatchaRequestFactory<WatchaBookMetaDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/users/%s/likes/decks";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final HttpClientResponseHandler<WatchaBookMetaDto> handler = WatchaBookMetaReponseHandler.create();

    private final static WatchaUserLikesBookcaseRequestFactory INSTANCE = new WatchaUserLikesBookcaseRequestFactory();

    private WatchaUserLikesBookcaseRequestFactory() {}

    public static WatchaUserLikesBookcaseRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookMetaDto> createRequest(final String value) {

        Request<WatchaBookMetaDto> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint);
        watchaRequest.setResponseHandler(handler);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint, classicHttpRequest -> {
                    classicHttpRequest.addHeader(new BasicHeader("Referer", "https://pedia.watcha.com/ko-KR/users/6ADvGrJpKqzZl/likes?type=decks"));
        });

            watchaRequest.setClientContext(clientContext -> {
            BasicCookieStore basicCookieStore = new BasicCookieStore();

            BasicClientCookie watchaUser1 = new BasicClientCookie("_c_pdi", "web-4LjHFv0QByiuzt0iSpyneTRSbjqmEo");
            watchaUser1.setPath("/");
            watchaUser1.setExpiryDate(Instant.parse("2026-05-12T04:09:59.424Z"));
            watchaUser1.setDomain(".watcha.com");

            BasicClientCookie watchaUser = new BasicClientCookie("_s_guit", "5ab1844576a521edc0d8719e5602a65f4badf4af6a1c30a1dae9c84aa35c");
            watchaUser.setPath("/");
            watchaUser.setDomain(".watcha.com");
            watchaUser.setExpiryDate(Instant.parse("2026-05-12T02:20:59.928Z"));
            watchaUser.setHttpOnly(true);

            basicCookieStore.addCookies(new Cookie[]{watchaUser, watchaUser1});

            clientContext.setCookieStore(basicCookieStore);
        });

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

}
