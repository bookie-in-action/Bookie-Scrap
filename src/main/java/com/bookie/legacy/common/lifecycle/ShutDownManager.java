package com.bookie.legacy.common.lifecycle;

import com.bookie.legacy.common.db.EntityManagerFactoryProvider;
import com.bookie.legacy.common.http.HttpClientProvider;
import com.bookie.legacy.common.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ShutDownManager {

    private static final List<Shutdownable> SHUTDOWNABLE_COMPONENTS = Arrays.asList(
//            DatabaseConnectionPool.getInstance(),
            EntityManagerFactoryProvider.getInstance(),
            HttpClientProvider.getInstance(),
            SchedulerManager.getInstance()
    );

    public void registerShutdownHooks() {
        for (Shutdownable component : SHUTDOWNABLE_COMPONENTS) {
            Thread shutDownThread = new Thread(() -> {
                try {
                    component.shutdown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Runtime.getRuntime().addShutdownHook(shutDownThread);
        }
    }
}
