package com.bookie.scrap.watcha.request.bookcomment;

import com.bookie.scrap.watcha.domain.WatchaRequestParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentCollectionService {

    private final BookCommentFetcher fetcher;
    private final BookCommentPersister persister;

    public void collect(String bookCode, WatchaRequestParam param) throws Exception{
            BookCommentResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

