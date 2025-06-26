package com.bookie.scrap.watcha.request.deck.deckinfo;

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
public class DeckInfoCollectionService implements WatchaCollectorService {

    private final DeckInfoFetcher fetcher;
    private final DeckInfoPersister persister;

    @Override
    @Transactional
    public int collect(String deckCode, PageInfo param) throws CollectionEx {
        try {
            DeckInfoResponseDto response = fetcher.fetch(deckCode, param);

            if (response == null) {
                log.warn("deckCode={} 의 deckInfo 수집 실패: fetch 결과가 null", deckCode);
                return 0;
            }

            try {
                return persister.persist(response, deckCode);
            } catch (RedisCommandTimeoutException | MongoTimeoutException e) {
                log.warn("deckCode={} deckInfo DB 연결 실패: {}", deckCode, e.getMessage());
                throw new RetriableCollectionEx("DB 연결 실패", e);
            }
        } catch (Exception e) {
            throw new CollectionEx("deckCode:" + deckCode + ":DeckInfoCollectionService", e);
        }
    }
}
