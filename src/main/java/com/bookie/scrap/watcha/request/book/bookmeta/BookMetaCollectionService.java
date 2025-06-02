package com.bookie.scrap.watcha.request.book.bookmeta;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMetaCollectionService implements WatchaCollectorService {

    private final BookMetaFetcher fetcher;
    private final BookMetaPersister persister;

    @Override
    public void collect(String bookCode, PageInfo param) throws Exception{
            BookMetaResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

