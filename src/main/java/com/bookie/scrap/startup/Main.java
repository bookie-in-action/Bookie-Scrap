package com.bookie.scrap.startup;

import com.bookie.scrap.config.watcha.WatchaBookConfig;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import com.bookie.scrap.util.DatabaseConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public class Main {
    public static void main(String[] args) {

        // -Dmode=dev or -Dmode=prod
        String serverMode = System.getProperty("mode", "dev");

        log.info("");
        log.info("*********************************************");
        log.info("   Initializing......                        ");
        log.info(" Running in {} MODE", serverMode.toUpperCase());
        log.info("       Bookie Scrap Version: 0.0.1-SNAPSHOT  ");
        log.info("                                             ");
        log.info("*********************************************");
        log.info("");

        // TODO: 잘라서 resources 로딩 로그, 라이브러리 로딩 로그 따로 출력 해야함
        String classPath = System.getProperty("java.class.path");
        log.info("Classpath: " + classPath);
        log.info("");

        /*

            1. db.properties로 커넥션 풀 초기화
            2. DB 관련 로그 출력
        */
        DatabaseConnectionPool.init(serverMode);


        //TODO: 스레드 예제, quartz 사용?, 데몬 스레드/비데몬 스레드 개념 조사,
        //TODO: 만약 quartz 사용한다면 quartz는 어떤 스레드로 돌려야 하는지,
        //TODO: sigterm(kill -15) sigkill(kill -9) 했을 때 jvm과 메인 스레드 종료 여부
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
