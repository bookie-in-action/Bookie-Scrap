package com.bookie.scrap.watcha.request.deck.booklist;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.booktodecks.BookToDecksResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public List<String> collectForTest(String deckCode, PageInfo param) throws CollectionEx {
        try {
            BookListResponseDto response = fetcher.fetch(deckCode, param);
            persister.persist(response, deckCode);
            return response.getResult().getBookCodes();
        } catch (Exception e) {
            throw MAKE("deckCode:" + deckCode + ":DeckCollectionService", e);
        }
    }
}
