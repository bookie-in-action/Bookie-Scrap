package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class BookToDecksCollectionService  implements WatchaCollectorService{

    private final BookToDecksFetcher fetcher;
    private final BookToDecksPersister persister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) throws CollectionEx {
        try {
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
        } catch (Exception e) {
            throw MAKE("bookCode:" + bookCode + ":BookToDecksCollectionService", e);
        }
    }

    @Transactional
    public List<String> collectForTest(String bookCode, PageInfo param) throws CollectionEx {
        try {
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
            return response.getResult().getDeckCodes();
        } catch (Exception e) {
            throw MAKE("bookCode:" + bookCode + ":BookToDecksCollectionService", e);
        }
    }
}
