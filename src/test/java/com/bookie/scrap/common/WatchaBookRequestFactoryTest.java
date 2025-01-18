package com.bookie.scrap.common;

import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.InitializableProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import com.bookie.scrap.watcha.config.WatchaBookRequestFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class WatchaBookRequestFactoryTest {
    @BeforeAll
    public static void init() {
        List<InitializableProperties> propertiesList = Arrays.asList(
                BookieProperties.getInstance(),
                DbProperties.getInstance(),
                SchedulerProperties.getInstance()
        );

        propertiesList.forEach(properties -> {
            properties.init("dev");
            properties.verify();
        });

    }

    @Test
    void createRequest() {
        RequestFactory watchaBookRequestFactory = new WatchaBookRequestFactory();
        Request watchaRequest = watchaBookRequestFactory.createRequest("byLKj8M");
        watchaRequest.execute();

    }
}

