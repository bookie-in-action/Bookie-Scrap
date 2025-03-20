package com.bookie.scrap.common.domain;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);

}
