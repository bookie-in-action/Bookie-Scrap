package com.bookie.scrap.common.scheduler;

import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(SchedulerStubConfig.class)
public class SimpleSchedulerStubTest {

    private SchedulerStubConfig.StubDynamicJobRegister stubDynamicJobRegister;

    @BeforeEach
    void setUp() {
        stubDynamicJobRegister = new SchedulerStubConfig.StubDynamicJobRegister();
        stubDynamicJobRegister.reset();
    }

    @Test
    void testWithStub() throws SchedulerException, ClassNotFoundException {
        stubDynamicJobRegister.registerJobs();
        Assertions.assertTrue(stubDynamicJobRegister.isRegisterJobsCalled());
    }
}
