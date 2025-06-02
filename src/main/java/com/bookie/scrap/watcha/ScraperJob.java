package com.bookie.scrap.watcha;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class ScraperJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
        // 비워도 됨
        System.out.println("[ScraperJob] 실행됨 (stub)");
    }
}

