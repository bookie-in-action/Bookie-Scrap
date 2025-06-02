package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWishBookCollectionService implements WatchaCollectorService {

    private final UserWishBookFetcher fetcher;
    private final UserWishBookPersister persister;

    public void collect(String bookCode, PageInfo param) throws Exception{
            UserWishBookResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

