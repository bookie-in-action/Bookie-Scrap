package com.bookie.scrap.common.scheduler;

import org.mockito.Mockito;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SchedulerStubConfig {

    @Bean
    @Primary
    public DynamicJobRegistrar testDynamicJobRegister() {
        return new StubDynamicJobRegister();
    }

    @Bean
    @Primary
    public Scheduler testScheduler() {
        return Mockito.mock(Scheduler.class);
    }

    static class StubDynamicJobRegister extends DynamicJobRegistrar {

        private boolean registerJobsCalled = false;

        // 실제 의존성 없이 생성
        public StubDynamicJobRegister() {
            super(Mockito.mock(Scheduler.class), new SchedulerProperties());
        }

        @Override
        public void registerJobs() throws SchedulerException, ClassNotFoundException {
            this.registerJobsCalled = true;
            // 실제 스케줄링 대신 플래그만 설정
        }

        public boolean isRegisterJobsCalled() {
            return registerJobsCalled;
        }

        public void reset() {
            this.registerJobsCalled = false;
        }
    }
}
