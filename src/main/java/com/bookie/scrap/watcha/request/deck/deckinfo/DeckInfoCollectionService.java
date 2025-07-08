package com.bookie.scrap.watcha.request.deck.deckinfo;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.common.exception.RetriableCollectionEx;
import com.bookie.scrap.common.redis.RedisStringListService;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.mongodb.MongoTimeoutException;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class DeckInfoCollectionService implements WatchaCollectorService {

    private final DeckInfoFetcher fetcher;
    private final DeckInfoPersister persister;
    private final RedisStringListService userRedisService;

    public DeckInfoCollectionService(
            DeckInfoFetcher fetcher,
            DeckInfoPersister persister,
            @Qualifier("pendingUserCode") RedisStringListService userRedisService
    ) {
        this.fetcher = fetcher;
        this.persister = persister;
        this.userRedisService = userRedisService;
    }

    @Override
    @Transactional
    public int collect(String deckCode, PageInfo param) throws CollectionEx {
        try {
            DeckInfoResponseDto response = fetcher.fetch(deckCode, param);

            if (response == null || response.getResult() == null) {
                log.warn("deckCode={} 의 deckInfo 수집 실패: fetch 결과가 null이거나 정보없음", deckCode);
                return 0;
            }

            try {
                int savedCnt = persister.persist(response, deckCode);
                if (savedCnt != 0) {
                    userRedisService.add(response.getResult().getUserCode());
                }

                log.info(
                        "deckCode={} deckInfo service page={} saved={}/{} success",
                        deckCode,
                        param.getPage(),
                        1,
                        savedCnt
                );

                return savedCnt;
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                throw new RetriableCollectionEx("deckCode=" + deckCode + " deckInfo DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("deckCode:" + deckCode + ":DeckInfoCollectionService", e);
        }
    }
}
