package com.bookie.scrap.config.watcha;

import lombok.Getter;
import com.bookie.scrap.http.HttpMethod;

@Getter
public class WatchaComment extends WatchaBaseRequest<String> {

    private final String HTTP_PROTOCOL = "https";
    private final String HTTP_HOST = "pedia.watcha.com";
//      "next_uri" : "/api/contents/byLKj8M/comments?filter=all&order=popular&page=2&size=8",
    private String HTTP_ENDPOINT = "/api/contents/%s/comments?page=%d&size=%d";
    private final int HTTP_PORT = 443;
    private final HttpMethod HTTP_METHOD = HttpMethod.GET;

    {
        initHttpHost();
        initStringHandler();
        setImplClassName(this.getClass().getSimpleName());
    }

    public WatchaComment(String bookCode, int page, int size) {
        this.HTTP_ENDPOINT = String.format(HTTP_ENDPOINT, bookCode, page, size);
        initHttpMethod();
    }

}
