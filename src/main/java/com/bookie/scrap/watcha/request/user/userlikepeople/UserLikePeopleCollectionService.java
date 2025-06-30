package com.bookie.scrap.watcha.request.user.userlikepeople;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLikePeopleCollectionService implements WatchaCollectorService {

    private final UserLikePeopleFetcher fetcher;
    private final UserLikePeoplePersister persister;

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserLikePeopleResponseDto response = fetcher.fetch(userCode, param);

            if (response == null) {
                log.warn("userCode={} 의 userLikePeople 수집 실패: fetch 결과가 null", userCode);
                return 0;
            }

            try {
                return persister.persist(response, userCode);
            } catch (MongoTimeoutException e) {
                log.warn("userCode={} userLikePeople DB 연결 실패: {}", userCode, e.getMessage());
                throw new RetriableCollectionEx("DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("userCode:" + userCode + ":UserLikePeopleCollectionService", e);
        }
    }
}
