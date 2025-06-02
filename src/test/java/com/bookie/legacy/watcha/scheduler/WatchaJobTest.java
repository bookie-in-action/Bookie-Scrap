package com.bookie.legacy.watcha.scheduler;

import com.bookie.legacy.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;

@Disabled
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