package com.bookie.scrap.watcha.request.user.userlikepeople;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLikePeopleCollectionService implements WatchaCollectorService {

    private final UserLikePeopleFetcher fetcher;
    private final UserLikePeoplePersister persister;

    public int collect(String userCode, PageInfo param) throws Exception{
            UserLikePeopleResponseDto response = fetcher.fetch(userCode, param);
            return persister.persist(response, userCode);
    }
}

