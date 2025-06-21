package com.bookie.legacy.common;

import com.bookie.legacy.common.db.EntityManagerFactoryProvider;
import com.bookie.legacy.common.properties.DbProperties;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@Disabled
class EntityManagerFactoryProviderTest {

    @Test
    void getEntityManagerFactory() {
        DbProperties.getInstance().init("dev");

        EntityManagerFactoryProvider.getInstance().init("dev");
        assertNotNull(EntityManagerFactoryProvider.getInstance().getEntityManagerFactory());
    }
}