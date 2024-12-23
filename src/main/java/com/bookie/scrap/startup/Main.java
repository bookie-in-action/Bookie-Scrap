package com.bookie.scrap.startup;

import com.bookie.scrap.config.watcha.WatchaBookConfig;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Slf4j
public class Main {
    public static void main(String[] args) {

        //TODO: 메인쪽은 전체 로직 따로 잡아야 함
        log.info("");
        log.info("*********************************************");
        log.info("   Initializing......                        ");
        log.info("       Bookie Scrap Version: 0.0.1-SNAPSHOT  ");
        log.info("                                             ");
        log.info("*********************************************");
        log.info("");

        // TODO: 잘라서 resources 로딩 로그, 라이브러리 로딩 로그 따로 출력 해야함
        String classPath = System.getProperty("java.class.path");
        log.info("Classpath: " + classPath);
        log.info("");

        // TODO: 후에 운영/개발 분리
        try (InputStream inpuStream = Main.class.getClassLoader().getResourceAsStream("db.properties")){
            Properties dbProperties = new Properties();
            dbProperties.load(inpuStream);
            log.info("DB URL: {}",dbProperties.getProperty("db.dev.url"));
            log.info("DB USER: {}",dbProperties.getProperty("db.dev.user"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        log.info("*********************************************");


        //TODO: 스레드 예제, quartz 사용?, 데몬 스레드/비데몬 스레드 개념 조사,
        //TODO: 만약 quartz 사용한다면 quartz는 어떤 스레드로 돌려야 하는지,
        //TODO: sigterm(kill -15) sigkill(kill -9) 했을 때 jvm과 메인 스레드 종료 여부
//        SchedulerManager.getInstance().initSchedulers();
//        SchedulerManager.getInstance().startSchedulers();
//
//        // Shutdown Hook 등록
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
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
