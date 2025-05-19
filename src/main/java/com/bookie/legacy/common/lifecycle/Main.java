package com.bookie.legacy.common.lifecycle;

import com.bookie.legacy.common.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;



@Slf4j
public class Main {
    /**
     * 1. 서버 초기화
     * 2. 스케줄러 시작
     * 3. 셧다운 훅 등록
     * @param args
     */
    public static void main(String[] args) {

        // -Dmode=dev or -Dmode=prod
        String serverMode = System.getProperty("mode", "dev");

        log.info("");
        log.info("************************************************");
        log.info("   Initializing......                           ");
        log.info("   Running in {} MODE", serverMode.toUpperCase() );
        log.info("   Bookie Scrap Version: 0S.0.1-SNAPSHOT         ");
        log.info("                                                ");
        log.info("************************************************");
        log.info("");

        new InitManager().init(serverMode);

        try {
            SchedulerManager.getInstance().startSchedulers();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

        new ShutDownManager().registerShutdownHooks();

    }
}
