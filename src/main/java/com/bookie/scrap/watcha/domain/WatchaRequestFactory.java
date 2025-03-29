package com.bookie.scrap.watcha.domain;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.Request;
import com.bookie.scrap.common.domain.RequestFactory;

public interface WatchaRequestFactory<T> extends RequestFactory<T> {

    default WatchaRequestParam asWatchaParam(PageInfo pageInfo) {
        if (pageInfo instanceof WatchaRequestParam) {
            return (WatchaRequestParam) pageInfo;
        }

        throw new IllegalArgumentException("Expected WatchaRequestParam, but got: " + pageInfo.getClass().getSimpleName());
    }

}
