package com.bookie.scrap.watcha.config;

import com.bookie.scrap.common.BaseRequest;
import org.apache.hc.core5.http.ClassicHttpRequest;

import java.util.Map;
import java.util.function.Function;


public abstract class WatchaBaseRequest<T> extends BaseRequest<T> {

    protected Map<String, String> watchaHeaders = Map.of(
            "Referer", "https://pedia.watcha.com",
            "X-Frograms-App-Code", "Galaxy",
            "X-Frograms-Client", "Galaxy-Web-App",
            "X-Frograms-Galaxy-Language", "ko",
            "X-Frograms-Galaxy-Region", "KR",
            "X-Frograms-Version", "2.1.0"
    );

    @Override
    protected void initHttpMethod() {
        Function<ClassicHttpRequest, ClassicHttpRequest> headerFunction = httpMethod -> {
            watchaHeaders.forEach(httpMethod::addHeader);
            return httpMethod;
        };

        super.setHttpMethod(headerFunction);
    }

}
