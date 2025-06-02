package com.bookie.scrap.watcha.request.user.userbookrating;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBookRatingCollectionService implements WatchaCollectorService {

    private final UserBookRatingFetcher fetcher;
    private final UserBookRatingPersister persister;

    public void collect(String userCode, PageInfo param) throws Exception {
            UserBookRatingResponseDto response = fetcher.fetch(userCode, param);
            persister.persist(response, userCode);
    }
}

