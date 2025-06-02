package com.bookie.scrap.common.http;


public interface ResponseHttpFactory<T> {
    SpringResponse<T> createSpringResponse();
}
