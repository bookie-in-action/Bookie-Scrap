package com.bookie.legacy.watcha.domain;

import com.bookie.legacy.common.domain.PageInfo;
import com.bookie.legacy.common.domain.RequestFactory;

public interface WatchaRequestFactory<T> extends RequestFactory<T> {

    default WatchaRequestParam asWatchaParam(PageInfo pageInfo) {
        if (pageInfo instanceof WatchaRequestParam) {
            return (WatchaRequestParam) pageInfo;
        }

        throw new IllegalArgumentException("Expected WatchaRequestParam, but got: " + pageInfo.getClass().getSimpleName());
    }

}
