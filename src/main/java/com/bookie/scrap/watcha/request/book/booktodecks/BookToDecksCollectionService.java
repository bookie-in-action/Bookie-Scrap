package com.bookie.scrap.watcha.request.book.booktodecks;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookToDecksCollectionService  implements WatchaCollectorService{

    private final BookToDecksFetcher fetcher;
    private final BookToDecksPersister persister;

    @Override
    public void collect(String bookCode, PageInfo param) throws Exception{
            BookToDecksResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

