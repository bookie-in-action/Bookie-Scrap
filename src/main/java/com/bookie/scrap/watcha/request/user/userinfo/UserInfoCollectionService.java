package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoCollectionService implements WatchaCollectorService {

    private final UserInfoFetcher fetcher;
    private final UserInfoPersister persister;

    public int collect(String bookCode, PageInfo param) throws Exception{
            UserInfoResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
    }
}

