package com.bookie.scrap.watcha.scheduler;

import com.bookie.scrap.watcha.entity.WatchaBookEntity;
import com.bookie.scrap.common.domain.Repository;
import com.bookie.scrap.watcha.repository.WatchaBookRepository;
import com.bookie.scrap.watcha.request.WatchaBookRequestFactory;
import com.bookie.scrap.watcha.dto.WatchaBookDto;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


@Slf4j
public class WatchaJob implements Job {

    private final Repository<WatchaBookEntity> bookRepository = WatchaBookRepository.getInstance();
    private final WatchaBookRequestFactory bookRequestFactory = WatchaBookRequestFactory.getInstance();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("watcha job executed!!!!!");

        WatchaBookDto bookDetail = bookRequestFactory.createRequest("byLKj8M").execute();
        boolean result = bookRepository.insertOrUpdate(bookDetail.toEntity());

    }
}
