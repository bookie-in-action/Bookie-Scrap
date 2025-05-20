package com.bookie.scrap.watcha;

import com.bookie.scrap.common.LegacyHttpMethodType;
import com.bookie.scrap.common.Request;
import lombok.Getter;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
public class WatchaRequest extends Request {

    @Getter
    private static final Map<String, String> WATCHA_HEADERS = Map.of(
            "Referer", "https://pedia.watcha.com",
            "X-Frograms-App-Code", "Galaxy",
            "X-Frograms-Client", "Galaxy-Web-App",
            "X-Frograms-Galaxy-Language", "ko",
            "X-Frograms-Galaxy-Region", "KR",
            "X-Frograms-Version", "2.1.0"
    );

    private static final HttpHeaders WATCHA_SPRING_HEADERS = new HttpHeaders();

    static {
        WATCHA_HEADERS.forEach(WATCHA_SPRING_HEADERS::add);
    }

    public WatchaRequest() {
        super.setImplClassName(this.getClass().getSimpleName());
    }

    @Override
    public void setMainRequest(LegacyHttpMethodType legacyHttpMethodType, String endPoint) {
        super.setHeaders(WATCHA_SPRING_HEADERS);
        super.setMainRequest(legacyHttpMethodType, endPoint);
    }

    @Override
    @Deprecated
    public void setMainRequest(LegacyHttpMethodType legacyHttpMethodType,
                               String endPoint,
                               Consumer<ClassicHttpRequest> consumer) {

        super.setHeaders(WATCHA_SPRING_HEADERS);

        Consumer<ClassicHttpRequest> combinedConsumer = applyWatchaHeaders().andThen(consumer);

        super.setMainRequest(legacyHttpMethodType, endPoint, combinedConsumer);
    }

    @Deprecated
    private Consumer<ClassicHttpRequest> applyWatchaHeaders() {
        return httpRequest -> WATCHA_HEADERS.forEach(httpRequest::addHeader);
    }

}
