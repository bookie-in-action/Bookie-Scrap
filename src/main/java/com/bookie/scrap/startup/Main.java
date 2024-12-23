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

        System.out.println("Is logback.xml loaded? " +
                LoggerFactory.getILoggerFactory().getClass().getName());


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

    }
}
