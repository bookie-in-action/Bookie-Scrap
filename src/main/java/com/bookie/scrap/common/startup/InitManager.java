package com.bookie.scrap.common.startup;

import com.bookie.scrap.common.db.EntityManagerFactoryProvider;
import com.bookie.scrap.common.http.HttpClientProvider;
import com.bookie.scrap.common.properties.BookieProperties;
import com.bookie.scrap.common.properties.DbProperties;
import com.bookie.scrap.common.properties.InitializableProperties;
import com.bookie.scrap.common.properties.SchedulerProperties;
import com.bookie.scrap.common.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class InitManager implements Initializable {

    private static final List<InitializableProperties> PROPERTIES = Arrays.asList(
            BookieProperties.getInstance(),
            DbProperties.getInstance(),
            SchedulerProperties.getInstance()
    );

    private static final List<Initializable> INITIALIZABLE_COMPONENTS = Arrays.asList(
//            DatabaseConnectionPool.getInstance(),
            EntityManagerFactoryProvider.getInstance(),
            HttpClientProvider.getInstance(),
            SchedulerManager.getInstance()
    );

    @Override
    public void init(String runningOption) {
        log.info("[STEP 1] Properties verify and initialize");

        PROPERTIES.forEach(properties -> {
            properties.init(runningOption);
            properties.verify();
        });

        log.info("[STEP 2] Initializing components");

        INITIALIZABLE_COMPONENTS.forEach(component -> {
            log.info(""); log.info("[Initializing component: {}]", component.getClass().getSimpleName());
            component.init(runningOption);
        });
    }

    public void devInit() {
        init("dev");
    }


}
