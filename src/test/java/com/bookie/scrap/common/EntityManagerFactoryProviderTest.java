package com.bookie.scrap.common;

import com.bookie.scrap.properties.DbProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityManagerFactoryProviderTest {

    @Test
    void getEntityManagerFactory() {
        DbProperties.getInstance().init("dev");

        EntityManagerFactoryProvider.getInstance().init("dev");
        assertNotNull(EntityManagerFactoryProvider.getInstance().getEntityManagerFactory());
    }
}