package com.bookie.scrap.watcha.request.userwishpeople;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWishPeopleCollectionService {

    private final UserWishPeopleFetcher fetcher;
    private final UserWishPeoplePersister persister;

    public void collect(String bookCode, WatchaRequestParam param) throws Exception{
            UserWishPeopleResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

