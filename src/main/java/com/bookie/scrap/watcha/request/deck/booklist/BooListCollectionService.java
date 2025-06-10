package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class BooListCollectionService implements WatchaCollectorService {

    private final BookListFetcher fetcher;
    private final BookListPersister persister;


    @Override
    @Transactional
    public int collect(String deckCode, PageInfo param) throws CollectionEx {
        try {
            BookListResponseDto response = fetcher.fetch(deckCode, param);
            return persister.persist(response, deckCode);
        } catch (Exception e) {
            throw MAKE("deckCode:" + deckCode + ":DeckCollectionService", e);
        }
    }
}
