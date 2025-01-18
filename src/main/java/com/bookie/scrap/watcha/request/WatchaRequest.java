package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.Request;
import com.bookie.scrap.http.HttpMethod;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.util.Map;
import java.util.function.Consumer;

public class WatchaRequest<T> extends Request<T> {

    private static final Map<String, String> WATCHA_HEADERS = Map.of(
            "Referer", "https://pedia.watcha.com",
            "X-Frograms-App-Code", "Galaxy",
            "X-Frograms-Client", "Galaxy-Web-App",
            "X-Frograms-Galaxy-Language", "ko",
            "X-Frograms-Galaxy-Region", "KR",
            "X-Frograms-Version", "2.1.0"
    );

    public WatchaRequest() {
        setImplClassName();
    }

    private Consumer<ClassicHttpRequest> applyWatchaHeaders() {
        return httpRequest -> WATCHA_HEADERS.forEach(httpRequest::addHeader);
    }

    @Override
    public void setMainRequest(HttpMethod httpMethod, String endPoint) {
        super.setMainRequest(httpMethod, endPoint);
        WATCHA_HEADERS.forEach(super.mainRequest::addHeader);
    }

    public void setMainRequest(HttpMethod httpMethod,
                               String endPoint,
                               Consumer<ClassicHttpRequest> consumer) {
        super.setMainRequest(httpMethod, endPoint);
        WATCHA_HEADERS.forEach(super.mainRequest::addHeader);
        consumer.accept(super.mainRequest);
    }

    @Override
    public void setImplClassName() {
        super.implClassName = this.getClass().getSimpleName();
    }
}
