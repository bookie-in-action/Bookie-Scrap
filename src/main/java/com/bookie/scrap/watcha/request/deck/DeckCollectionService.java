package com.bookie.scrap.watcha.request.deck;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.domain.redis.RedisStringListService;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class DeckCollectionService implements WatchaCollectorService {

    private final DeckFetcher fetcher;
    private final DeckPersister persister;


    @Override
    @Transactional
    public int collect(String deckCode, PageInfo param) throws CollectionEx {
        try {
            DeckResponseDto response = fetcher.fetch(deckCode, param);
            return persister.persist(response, deckCode);
        } catch (Exception e) {
            throw MAKE("deckCode:" + deckCode + ":DeckCollectionService", e);
        }
    }
}
