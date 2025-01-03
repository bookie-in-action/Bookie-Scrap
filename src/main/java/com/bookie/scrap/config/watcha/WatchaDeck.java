package com.bookie.scrap.config.watcha;

import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;

@Getter
public class WatchaDeck extends WatchaBaseRequest<String> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "pedia.watcha.com";
    private String HTTP_ENDPOINT = "/api/contents/%s/decks?page=%d&size=%d";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initHttpHost();
        initStringHandler();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaDeck(String bookCode, int page, int size) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode, page, size);
        initHttpMethod();
    }

}
