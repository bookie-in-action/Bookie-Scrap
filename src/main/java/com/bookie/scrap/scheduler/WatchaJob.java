package com.bookie.scrap.scheduler;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaBookDetailDTO;
import com.bookie.scrap.watcha.repository.WatchaBookJDBCImpl;
import com.bookie.scrap.watcha.repository.WatchaBookRepository;
import com.bookie.scrap.watcha.response.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Optional;

@Slf4j
public class WatchaJob implements Job {

    WatchaBookRepository watchaBookRepository = WatchaBookJDBCImpl.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 1. DECK에서 책 가져와서 BFS로 큐에 담기


        // 2. 담은 큐에서 꺼내서 요청 보낸 후 책 메타 정보 저장
        log.info("watcha job executed!!!!!");
//        WatchaBookDetail bookDetail = HttpRequestExecutor.execute(new WatchaBook(""));
//        Optional<WatchaBookDetailDTO> selectedBook = watchaBookRepository.select(bookDetail.getCode());
//
//        if(selectedBook.isPresent()) {
//            if (!selectedBook.equals(bookDetail)) {
//                watchaBookRepository.update(bookDetail.toDto());
//            }
//        } else {
//            watchaBookRepository.insert(bookDetail.toDto());
//        }

        // 3. 관련 사용자 리뷰 저장
    }
}
