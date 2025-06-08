package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentCollectionService implements WatchaCollectorService {

    private final BookCommentFetcher fetcher;
    private final BookCommentPersister persister;

    @Override
    public int collect(String bookCode, PageInfo param) throws Exception{
            BookCommentResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
    }
}

