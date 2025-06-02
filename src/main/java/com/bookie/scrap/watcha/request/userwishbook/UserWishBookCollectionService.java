package com.bookie.scrap.watcha.request.userwishbook;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWishBookCollectionService {

    private final UserWishBookFetcher fetcher;
    private final UserWishBookPersister persister;

    public void collect(String bookCode, WatchaRequestParam param) throws Exception{
            UserWishBookResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

