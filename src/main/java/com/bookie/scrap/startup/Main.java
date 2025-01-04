package com.bookie.scrap.startup;

import com.bookie.scrap.http.HttpClientProvider;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.util.DatabaseConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


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

        log.info("[STEP 1] Properties initialize and verify");
        List<InitializableProperties> propertiesList = Arrays.asList(
                        BookieProperties.getInstance(),
                        DbProperties.getInstance(),
                        SchedulerProperties.getInstance()
        );

        propertiesList.forEach(properties -> {
            properties.init(serverMode);
            properties.verify();
        });


        log.info("[STEP 2] DB Pool initialize");
        DatabaseConnectionPool.getInstance().init();

        log.info("[STEP 3] Http Connection Pool initialize");
        HttpClientProvider.init();

        // TODO: 유휴 http 커넥션 정리하는 스레드 생성
        // TODO: 스케줄러 생성
//        SchedulerManager.getInstance().initSchedulers();
//        SchedulerManager.getInstance().startSchedulers();

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


        // 메인 스레드 유지
        while (true) {
            try {
                Thread.sleep(3000);
                log.info("Thread Example running...");
            } catch (InterruptedException e) {
                break;
            }
        }

    }
}
