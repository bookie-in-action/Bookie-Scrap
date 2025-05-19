package com.bookie.scrap.watcha.scheduler;

import com.bookie.legacy.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;

class WatchaJobTest {

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

//    @Test
//    void test() throws JobExecutionException {
//        WatchaJob watchaJob = new WatchaJob();
//        watchaJob.execute();
//    }

}