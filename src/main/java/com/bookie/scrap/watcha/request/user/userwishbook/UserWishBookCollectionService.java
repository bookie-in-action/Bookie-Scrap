package com.bookie.scrap.watcha.request.user.userwishbook;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWishBookCollectionService implements WatchaCollectorService {

    private final UserWishBookFetcher fetcher;
    private final UserWishBookPersister persister;

    @Transactional
    public int collect(String bookCode, PageInfo param) throws Exception {
            UserWishBookResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
    }
}

