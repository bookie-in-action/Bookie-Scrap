package com.bookie.scrap;

import com.bookie.scrap.common.redis.RedisConnectionService;
import com.bookie.scrap.common.scheduler.DynamicJobRegistrar;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@AllArgsConstructor
@SpringBootApplication
public class ScrapApp implements CommandLineRunner {

    private final DynamicJobRegistrar jobRegistrar;
    private final RedisConnectionService redisConnectionService;

    public static void main(String[] args) {
        SpringApplication.run(ScrapApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        redisConnectionService.delete();
        jobRegistrar.registerJobs();
    }
}
