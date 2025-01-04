package com.bookie.scrap.service;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaBookDetailDTO;
import com.bookie.scrap.watcha.repository.WatchaBookJDBCImpl;
import com.bookie.scrap.watcha.repository.WatchaBookRepository;
import com.bookie.scrap.watcha.response.WatchaBookDetail;

import java.util.Optional;

public class WatchaService implements Service{

    WatchaBookRepository watchaBookRepository = WatchaBookJDBCImpl.getInstance();

    @Override
    public void scrap() {

        // 1. DECK에서 책 가져와서 BFS로 큐에 담기


        // 2. 담은 큐에서 꺼내서 요청 보낸 후 책 메타 정보 저장
        WatchaBookDetail bookDetail = HttpRequestExecutor.execute(new WatchaBook(""));
        Optional<WatchaBookDetailDTO> selectedBook = watchaBookRepository.select(bookDetail.getCode());

        if(selectedBook.isPresent()) {
            if (!selectedBook.equals(bookDetail)) {
                watchaBookRepository.update(bookDetail.toDto());
            }
        } else {
            watchaBookRepository.insert(bookDetail.toDto());
        }

        // 3. 관련 사용자 리뷰 저장



    }

}
