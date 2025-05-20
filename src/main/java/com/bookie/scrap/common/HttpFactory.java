package com.bookie.scrap.common;

public interface HttpFactory<T> {
    Request createRequest(final String value);

    default Request createRequest(final String value, PageInfo pageInfo) {
        throw new RuntimeException("Failed to create request: Method must be overridden.");
    }

    T createResponse();

}
