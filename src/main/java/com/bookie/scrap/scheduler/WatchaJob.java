package com.bookie.scrap.scheduler;

import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.watcha.config.WatchaBook;
import com.bookie.scrap.watcha.dto.WatchaEntity;
import com.bookie.scrap.watcha.repository.WatchaHibenateImpl;
import com.bookie.scrap.watcha.repository.WatchaRepository;
import com.bookie.scrap.watcha.response.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@Slf4j
public class WatchaJob implements Job {

//    WatchaBookRepository watchaBookRepository = WatchaBookJDBCImpl.getInstance();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        // 1. DECK에서 책 가져와서 BFS로 큐에 담기


        // 2. 담은 큐에서 꺼내서 요청 보낸 후 책 메타 정보 저장
        log.info("watcha job executed!!!!!");
        WatchaRepository bookRepo = WatchaHibenateImpl.getInstance(WatchaEntity.class);


            // 1. DECK에서 책 가져와서 큐에 담기


            // 2. 담은 큐에서 꺼내서 요청 보낸 후 책 메타 정보 저장
            WatchaBookDetail bookDetail = HttpRequestExecutor.execute(new WatchaBook(""));
            boolean result = bookRepo.insertOrUpdate(bookDetail.toEntity());

    }
}
