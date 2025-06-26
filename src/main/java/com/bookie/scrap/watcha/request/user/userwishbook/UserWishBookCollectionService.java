package com.bookie.scrap.watcha.request.user.userwishbook;

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
public class UserWishBookCollectionService implements WatchaCollectorService {

    private final UserWishBookFetcher fetcher;
    private final UserWishBookPersister persister;

    @Override
    @Transactional
    public int collect(String userCode, PageInfo param) throws CollectionEx {
        try {
            UserWishBookResponseDto response = fetcher.fetch(userCode, param);

            if (response == null) {
                log.warn("userCode={} 의 userWishBook 수집 실패: fetch 결과가 null", userCode);
                return 0;
            }

            try {
                return persister.persist(response, userCode);
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                log.warn("userCode={} userWishBook DB 연결 실패: {}", userCode, e.getMessage());
                throw new RetriableCollectionEx("DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("userCode:" + userCode + ":UserWishBookCollectionService", e);
        }
    }
}
