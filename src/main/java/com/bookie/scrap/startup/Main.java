package com.bookie.scrap.startup;

import com.bookie.scrap.config.watcha.WatchaBookConfig;
import com.bookie.scrap.http.HttpRequestExecutor;
import com.bookie.scrap.response.watcha.WatchaBookDetail;
import lombok.extern.slf4j.Slf4j;


//@Slf4j
public class Main {
    public static void main(String[] args) {
        String logbackPath = System.getProperty("logback.configurationFile");
        String dbPropertiesPath = System.getProperty("db.properties");

        String classPath = System.getProperty("java.class.path");
        System.out.println("Classpath: " + classPath);

//        System.out.println("Logback Path: " + logbackPath);
//        System.out.println("DB Properties Path: " + dbPropertiesPath);
//        WatchaBookDetail book = HttpRequestExecutor.execute(new WatchaBookConfig("byLKj8M"));

//        log.info("");
//        log.info("*********************************************");
//        log.info("   Initializing......                        ");
//        log.info("       Bookie Scrap Version: 0.0.1-SNAPSHOT  ");
//        log.info("                                             ");
//        log.info("*********************************************");
//        log.info("");
    }
}
