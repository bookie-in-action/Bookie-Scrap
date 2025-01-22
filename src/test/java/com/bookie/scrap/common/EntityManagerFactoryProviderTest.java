package com.bookie.scrap.common;

import com.bookie.scrap.properties.BookieProperties;
import com.bookie.scrap.properties.DbProperties;
import com.bookie.scrap.properties.SchedulerProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityManagerFactoryProviderTest {

    @Test
    void getEntityManagerFactory() {
        DbProperties.getInstance().init("dev");

        EntityManagerFactoryProvider.getInstance().init();
        assertNotNull(EntityManagerFactoryProvider.getInstance().getEntityManagerFactory());
    }
}