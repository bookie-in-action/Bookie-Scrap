package com.bookie.scrap;

import com.bookie.scrap.common.DynamicJobRegistrar;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
public class ScrapApp implements CommandLineRunner {

    private final DynamicJobRegistrar jobRegistrar;

    public static void main(String[] args) {
        SpringApplication.run(ScrapApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobRegistrar.registerJobs();
    }
}
