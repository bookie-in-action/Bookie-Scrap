package com.bookie.scrap.common;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);
}
