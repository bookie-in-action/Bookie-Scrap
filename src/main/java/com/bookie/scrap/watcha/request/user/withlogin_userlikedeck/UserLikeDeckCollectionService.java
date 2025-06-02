package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLikeDeckCollectionService implements WatchaCollectorService {

    private final UserLikeDeckFetcher fetcher;
    private final UserLikeDeckPersister persister;

    public void collect(String bookCode, PageInfo param) throws Exception{
            UserLikeDeckResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

