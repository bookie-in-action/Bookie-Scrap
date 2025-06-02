package com.bookie.scrap.watcha.request.user.userwishpeople;

import com.bookie.scrap.common.domain.PageInfo;
import com.bookie.scrap.watcha.domain.WatchaCollectorService;
import com.bookie.scrap.watcha.request.book.bookcomment.WatchaBookCommentParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWishPeopleCollectionService implements WatchaCollectorService {

    private final UserWishPeopleFetcher fetcher;
    private final UserWishPeoplePersister persister;

    public void collect(String bookCode, PageInfo param) throws Exception{
            UserWishPeopleResponseDto response = fetcher.fetch(bookCode, param);
            persister.persist(response, bookCode);
    }
}

