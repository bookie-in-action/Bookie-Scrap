package com.bookie.scrap.watcha.request;

import com.bookie.scrap.common.request.Request;
import com.bookie.scrap.common.http.HttpMethod;
import lombok.Getter;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.util.Map;
import java.util.function.Consumer;

public class WatchaRequest<T> extends Request<T> {

    @Getter
    private static final Map<String, String> WATCHA_HEADERS = Map.of(
            "Referer", "https://pedia.watcha.com",
            "X-Frograms-App-Code", "Galaxy",
            "X-Frograms-Client", "Galaxy-Web-App",
            "X-Frograms-Galaxy-Language", "ko",
            "X-Frograms-Galaxy-Region", "KR",
            "X-Frograms-Version", "2.1.0"
    );

    public WatchaRequest() {
        setImplClassName(this.getClass().getSimpleName());
    }

    private Consumer<ClassicHttpRequest> applyWatchaHeaders() {
        return httpRequest -> WATCHA_HEADERS.forEach(httpRequest::addHeader);
    }

    @Override
    public void setMainRequest(HttpMethod httpMethod, String endPoint) {
        super.setMainRequest(httpMethod, endPoint, this.applyWatchaHeaders());
    }

    @Override
    public void setMainRequest(HttpMethod httpMethod,
                               String endPoint,
                               Consumer<ClassicHttpRequest> consumer) {

        Consumer<ClassicHttpRequest> combinedConsumer = applyWatchaHeaders().andThen(consumer);

        super.setMainRequest(httpMethod, endPoint, combinedConsumer);
    }

}
