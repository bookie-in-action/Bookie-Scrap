package com.bookie.scrap.startup;

import com.bookie.scrap.domain.Initializer;
import com.bookie.scrap.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;



@Slf4j
public class Main {
    public static void main(String[] args) {

        // -Dmode=dev or -Dmode=prod
        String serverMode = System.getProperty("mode", "dev");

        log.info("");
        log.info("************************************************");
        log.info("   Initializing......                           ");
        log.info("   Running in {} MODE", serverMode.toUpperCase() );
        log.info("   Bookie Scrap Version: 0.0.1-SNAPSHOT         ");
        log.info("                                                ");
        log.info("************************************************");
        log.info("");


        new Initializer().init(serverMode);




        // TODO: 유휴 http 커넥션 정리하는 스레드 생성
        // TODO: 스케줄러 생성
        try {
            SchedulerManager.getInstance().startSchedulers();
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

//        // Shutdown Hook 등록
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                1. db 커넥션 풀 닫기
//                System.out.println("Shutting down connection pool...");
//                DatabaseConnectionPool.close();
//                2. 스케줄러 종료

//                SchedulerManager.getInstance().stopSchedulers();
//                System.out.println("Scheduler stopped gracefully.");

//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }));


    }
}
