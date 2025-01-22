package com.bookie.scrap.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DbPropertiesTest {

    @Test
    void init() {
        DbProperties dbProperties = DbProperties.getInstance();
        dbProperties.init("dev");

        Assertions.assertEquals("10", dbProperties.getValue(DbProperties.Key.MAX_POOL));
        Assertions.assertEquals("bookie_dev", dbProperties.getValue(DbProperties.Key.USER));

    }
}