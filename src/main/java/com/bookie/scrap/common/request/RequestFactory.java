package com.bookie.scrap.common.request;

import com.bookie.scrap.watcha.dto.WatchaBaseRequestParamDTO;

public interface RequestFactory<T> {
    Request<T> createRequest(final String value);

    default Request<T> createRequest(final WatchaBaseRequestParamDTO watchaRequestParamDTO) {
        return null;
    }
}
