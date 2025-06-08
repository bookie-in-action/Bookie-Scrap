package com.bookie.scrap.watcha.domain;

import com.bookie.scrap.common.domain.PageInfo;

public interface WatchaCollectorService {
    int collect(String bookCode, PageInfo param) throws Exception;
}
