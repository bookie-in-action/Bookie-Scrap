package com.bookie.scrap.watcha.request.userinfo;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoCollectionService {

    private final UserInfoFetcher fetcher;
    private final UserInfoPersister persister;

    public void collect(String bookCode, WatchaRequestParam param) throws Exception{
            UserInfoResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

