package com.bookie.scrap.watcha.scheduler;

import com.bookie.scrap.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class WatchaJobTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    void test() throws JobExecutionException {
        WatchaJob watchaJob = new WatchaJob();
        watchaJob.execute();
    }

}