package com.bookie.scrap.watcha.request.book.bookcomment;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.common.exception.CollectionEx;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookie.scrap.common.exception.CollectionEx.MAKE;

@Service
@RequiredArgsConstructor
public class BookCommentCollectionService implements WatchaCollectorService {

    private final BookCommentFetcher fetcher;
    private final BookCommentPersister persister;

    @Override
    @Transactional
    public int collect(String bookCode, PageInfo param) throws CollectionEx {
        try {
            BookCommentResponseDto response = fetcher.fetch(bookCode, param);
            return persister.persist(response, bookCode);
        } catch (Exception e) {
            throw MAKE("bookCode:" + bookCode + ":BookCommentCollectionService", e);
        }
    }

}

