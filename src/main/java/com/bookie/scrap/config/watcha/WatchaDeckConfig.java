package com.bookie.scrap.config.watcha;

import com.bookie.scrap.config.BaseRequestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;
import com.bookie.scrap.http.HttpResponseWrapper;

import java.util.function.Function;

@Getter
public class WatchaDeckConfig extends BaseRequestConfig<String> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/contents/%s/decks?page=%d&size=%d";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        createHttpHost();
        initStringHandler();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaDeckConfig(String bookCode, int page, int size) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode, page, size);
        initWatchaHttpMethod();
    }

}
