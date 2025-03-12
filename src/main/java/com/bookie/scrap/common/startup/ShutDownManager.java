package com.bookie.scrap.common.startup;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.http.HttpClientProvider;
import com.bookie.scrap.common.scheduler.SchedulerManager;
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
