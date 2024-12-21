package com.bookie.scrap.config;

import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpResponseWrapper;

import java.util.function.Function;

@Getter
public class WatchaExternalUrlConfig extends BaseRequestConfig<String> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "redirect.watcha.com";
    private String HTTP_ENDPOINT = "/galaxy/%s";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        createHttpHost();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaExternalUrlConfig(String externalBookCode) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, externalBookCode);
        initializeExternalResponseHandler();
        initWatchaHttpMethod();
    }

    protected void initializeExternalResponseHandler() {

        Function<HttpResponseWrapper, String> LocationHeaderResponse =
                responseWrapper -> responseWrapper.findHeader("location").get().getValue();

        createResponseHandler(LocationHeaderResponse);

    }


}
