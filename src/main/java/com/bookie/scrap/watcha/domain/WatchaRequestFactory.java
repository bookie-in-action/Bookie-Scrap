package com.bookie.scrap.watcha.domain;

import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.domain.RequestFactory;

public interface WatchaRequestFactory<T> extends RequestFactory<T> {
    default Request<T> createRequest(final String value, final WatchaRequestParam requestParam) {
        throw new RuntimeException("Failed to create request: Method must be overridden.");
    }
}
