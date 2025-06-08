package com.bookie.scrap.watcha.request.user.userinfo;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class UserInfoCollectionService implements WatchaCollectorService {

    private final UserInfoFetcher fetcher;
    private final UserInfoPersister persister;

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserInfoResponseDto response = fetcher.fetch(userCode, param);
            return persister.persist(response, userCode);
        } catch (Exception e) {
            throw MAKE("userCode:" + userCode + ":UserInfoCollectionService", e);
        }
    }
}
