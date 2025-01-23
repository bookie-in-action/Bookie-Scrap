package com.bookie.scrap.common.request;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);
}
