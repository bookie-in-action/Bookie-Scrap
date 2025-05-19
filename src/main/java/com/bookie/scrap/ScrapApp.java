package com.bookie.scrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrapApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScrapApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
