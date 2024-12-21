package com.bookie.scrap.config.watcha;

import com.bookie.scrap.config.BaseRequestConfig;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpResponseWrapper;
import lombok.Getter;

import java.util.function.Function;

@Getter
public class KyoboUrlConfig extends BaseRequestConfig<String> {

    private final String HTTP_PROTOCOL = "https";
    private String HTTP_HOST = "";
    private String HTTP_ENDPOINT = "";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        setImplClassName(this.getClass().getSimpleName());
        initStringHandler();
    }

    public KyoboUrlConfig(String host, String url) {
        this.HTTP_HOST = host;
        this.HTTP_ENDPOINT = url;
        createHttpHost();
        initHttpMethod();
        initCustomHandler();
    }

    protected void initCustomHandler() {

        Function<HttpResponseWrapper, String> LocationHeaderResponse =
                responseWrapper -> responseWrapper.findHeader("location").get().getValue();

        createResponseHandler(LocationHeaderResponse);

    }



}
