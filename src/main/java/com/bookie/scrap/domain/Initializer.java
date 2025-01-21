package com.bookie.scrap.domain;

import com.bookie.scrap.common.DatabaseConnectionPool;
import com.bookie.scrap.http.HttpClientProvider;
import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class Initializer implements Initializable {

    List<InitializableProperties> propertiesList = Arrays.asList(
            BookieProperties.getInstance(),
            DbProperties.getInstance(),
            SchedulerProperties.getInstance()
    );

    @Override
    public void init(String runningOption) {

        log.info("[STEP 1] Properties verify and initialize");

        propertiesList.forEach(properties -> {
            properties.init(runningOption);
            properties.verify();
        });

        log.info("[STEP 2] DB Pool initialize");
        DatabaseConnectionPool.getInstance().init(runningOption);

        log.info("[STEP 3] Http Connection Pool initialize");
        HttpClientProvider.getInstance().init(runningOption);

    }
}
