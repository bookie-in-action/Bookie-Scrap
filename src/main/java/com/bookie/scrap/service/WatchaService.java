package com.bookie.scrap.service;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaEntity;
import com.bookie.scrap.watcha.repository.WatchaHibenateImpl;
import com.bookie.scrap.watcha.repository.WatchaRepository;
import com.bookie.scrap.watcha.response.WatchaBookDetail;


public class WatchaService implements Service{

    WatchaRepository bookRepo = WatchaHibenateImpl.getInstance(WatchaEntity.class);

    @Override
    public void scrap() {

        // 1. DECK에서 책 가져와서 큐에 담기


        // 2. 담은 큐에서 꺼내서 요청 보낸 후 책 메타 정보 저장
        WatchaBookDetail bookDetail = HttpRequestExecutor.execute(new WatchaBook(""));
        boolean result = bookRepo.insertOrUpdate(bookDetail.toEntity());

    }

}
