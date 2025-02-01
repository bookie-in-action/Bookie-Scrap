package com.bookie.scrap.service;

import com.bookie.scrap.watcha.dto.WatchaCommentDetailDTO;
import com.bookie.scrap.watcha.repository.WatchaCommentJDBCImpl;
import com.bookie.scrap.watcha.repository.WatchaCommentRepository;

import java.util.Optional;

public class WatchaCommentService implements Service{

    WatchaCommentRepository watchaCommentRepository = WatchaCommentJDBCImpl.getInstance();

    @Override
    public void scrap() {
        //
        //WatchaCommentDetail CommentDetail = HttpRequestExecutor.execute(new WatchaCommentDetail(new WatchaComment("byLKj8M", 1, 10)));
    }

    public Optional<WatchaCommentDetailDTO> getDetail(String bookCode) {
        // 데이터 조회
        return  watchaCommentRepository.select(bookCode);
    }
}
