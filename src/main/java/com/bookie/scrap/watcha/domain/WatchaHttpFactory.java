package com.bookie.scrap.watcha.domain;


import com.bookie.scrap.common.http.ResponseHttpFactory;
import com.bookie.scrap.common.http.SpringRequest;

public interface WatchaHttpFactory<T> extends ResponseHttpFactory<T> {
    SpringRequest createSpringRequest(String endpoint);

    void execute(String bookCode, WatchaRequestParam param);
}
