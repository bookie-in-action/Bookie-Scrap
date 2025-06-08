package com.bookie.scrap.watcha.request.user.withlogin_userlikedeck;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class UserLikeDeckCollectionService implements WatchaCollectorService {

    private final UserLikeDeckFetcher fetcher;
    private final UserLikeDeckPersister persister;

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserLikeDeckResponseDto response = fetcher.fetch(userCode, param);
            return persister.persist(response, userCode);
        } catch (Exception e) {
            throw MAKE("userCode:" + userCode + ":UserLikeDeckCollectionService", e);
        }
    }
}
