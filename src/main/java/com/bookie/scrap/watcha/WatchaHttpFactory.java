package com.bookie.scrap.watcha;


import com.bookie.scrap.common.Request;
import com.bookie.scrap.common.HttpFactory;

public interface WatchaHttpFactory<T> extends HttpFactory<T> {

    default Request createRequest(final String value, WatchaRequestParam watchaRequestParam) {
        throw new RuntimeException("Failed to create request: Method must be overridden.");
    }

}
