package com.bookie.scrap.watcha.request.deck;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeckCollectionService implements WatchaCollectorService {

    private final DeckFetcher fetcher;
    private final DeckPersister persister;

    @Override
    public int collect(String bookCode, PageInfo param) throws Exception{
            DeckResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
    }
}

