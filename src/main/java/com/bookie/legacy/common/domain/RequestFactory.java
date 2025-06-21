package com.bookie.legacy.common.domain;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);

    default Request<T> createRequest(final String value, PageInfo pageInfo) {
        throw new RuntimeException("Failed to create request: Method must be overridden.");
    }

}
