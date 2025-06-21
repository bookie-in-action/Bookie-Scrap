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
import org.junit.jupiter.api.Disabled;

import java.time.Instant;

@Disabled
@Slf4j
public class WatchaUserAnalysisRequestFactory implements WatchaRequestFactory<WatchaBookMetaDto> {

    private final String HTTP_URL_PATTERN ="https://pedia.watcha.com/api/users/%s";
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;
    private final HttpClientResponseHandler<WatchaBookMetaDto> handler = WatchaBookMetaReponseHandler.create();

    private final static WatchaUserAnalysisRequestFactory INSTANCE = new WatchaUserAnalysisRequestFactory();

    private WatchaUserAnalysisRequestFactory() {}

    public static WatchaUserAnalysisRequestFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public Request<WatchaBookMetaDto> createRequest(final String value) {

        Request<WatchaBookMetaDto> watchaRequest = new WatchaRequest<>();
        String endPoint = String.format(HTTP_URL_PATTERN, value);

        watchaRequest.setMainRequest(HTTP_METHOD, endPoint, classicHttpRequest -> {
            classicHttpRequest.addHeader(new BasicHeader("Referer", "https://pedia.watcha.com/ko-KR/users/6ADvGrJpKqzZl"));
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Device-Identifier", "web-4LjHFv0QByiuzt0iSpyneTRSbjqmEo"));
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Remote-Addr", "1.209.229.179"));

            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Version", "2.1.0"));
//
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Client-Language", "ko"));
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Client-Region", "KR"));
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Client-Version", "2.1.0"));
            classicHttpRequest.addHeader(new BasicHeader("X-Frograms-Device-Name", "Edge:135.0.0.0 Windows:NT 10.0"));
//            classicHttpRequest.addHeader(new BasicHeader("accept", "application/vnd.frograms+json;version=2.1.0"));
//            classicHttpRequest.addHeader(new BasicHeader("accept-encoding", "gzip, deflate, br, zstd"));
//            classicHttpRequest.addHeader(new BasicHeader("accept-language", "ko,en;q=0.9,en-US;q=0.8"));
//            classicHttpRequest.addHeader(new BasicHeader("cache-control", "no-cache"));
//            classicHttpRequest.addHeader(new BasicHeader("pragma", "no-cache"));
//            classicHttpRequest.addHeader(new BasicHeader("priority", "u=1, i"));
//            classicHttpRequest.addHeader(new BasicHeader("sec-ch-ua", "Microsoft Edge\";v=\"135\", \"Not-A.Brand\";v=\"8\", \"Chromium\";v=\"135"));
//            classicHttpRequest.addHeader(new BasicHeader("sec-ch-ua-mobile", "?0"));
//            classicHttpRequest.addHeader(new BasicHeader("sec-ch-ua-platform", "\"Windows\""));
//            classicHttpRequest.addHeader(new BasicHeader("sec-fetch-dest", "empty"));
//            classicHttpRequest.addHeader(new BasicHeader("sec-fetch-mode", "cors"));
//            classicHttpRequest.addHeader(new BasicHeader("sec-fetch-site", "same-origin"));
            classicHttpRequest.addHeader(new BasicHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36 Edg/135.0.0.0"));
            classicHttpRequest.addHeader(new BasicHeader("X-Forwarded-For", "null"));
        });

        watchaRequest.setClientContext(clientContext -> {
            BasicCookieStore basicCookieStore = new BasicCookieStore();
//            BasicClientCookie watchaLang = new BasicClientCookie("Watcha-Web-Client-Language", "ko");
//            watchaLang.setPath("/");
//            watchaLang.setDomain(".watcha.com");
//
//            BasicClientCookie watchaRegion = new BasicClientCookie("Watcha-Web-Client-Region", "KR");
//            watchaRegion.setPath("/");
//            watchaRegion.setDomain(".watcha.com");
//
            BasicClientCookie watchaUser1 = new BasicClientCookie("_c_pdi", "web-4LjHFv0QByiuzt0iSpyneTRSbjqmEo");
            watchaUser1.setPath("/");
            watchaUser1.setExpiryDate(Instant.parse("2026-05-12T04:09:59.424Z"));
            watchaUser1.setDomain(".watcha.com");

//            BasicClientCookie watchaUser2 = new BasicClientCookie("_ga_1PYHGTCRYW", "GS1.1.1744001754.1.0.1744001754.0.0.0");
//            watchaUser1.setPath("/");
//            watchaUser1.setExpiryDate(Instant.parse("2026-05-12T04:09:59.424Z"));
//            watchaUser1.setDomain(".watcha.com");
//
//            BasicClientCookie watchaUser3 = new BasicClientCookie("_ga", "GA1.1.1486071032.1744001755");
//            watchaUser1.setPath("/");
//            watchaUser1.setExpiryDate(Instant.parse("2026-05-12T04:09:59.424Z"));
//            watchaUser1.setDomain(".watcha.com");

            BasicClientCookie watchaUser = new BasicClientCookie("_s_guit", "5ab1844576a521edc0d8719e5602a65f4badf4af6a1c30a1dae9c84aa35c");
            watchaUser.setPath("/");
            watchaUser.setDomain(".watcha.com");
            watchaUser.setExpiryDate(Instant.parse("2026-05-12T02:20:59.928Z"));
            watchaUser.setHttpOnly(true);

            basicCookieStore.addCookies(new Cookie[]{watchaUser, watchaUser1});

            clientContext.setCookieStore(basicCookieStore);
        });

        watchaRequest.setResponseHandler(handler);

        log.debug("Created WatchaRequest value: {}, endpoint: {}, method: {}", value, endPoint, HTTP_METHOD);

        return watchaRequest;
    }

}
