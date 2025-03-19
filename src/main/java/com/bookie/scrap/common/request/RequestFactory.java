package com.bookie.scrap.common.request;

import com.bookie.scrap.watcha.domain.WatchaBaseRequestParam;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);

    default Request<T> createRequest(final WatchaBaseRequestParam watchaRequestParamDTO) {
        throw new RuntimeException("Failed to create request: Method must be overridden.");
    }
}
