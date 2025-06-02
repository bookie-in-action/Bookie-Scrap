package com.bookie.legacy.common.properties;

import com.bookie.legacy.common.properties.DbProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
@Disabled
class DbPropertiesTest {

    @Test
    void init() {
        DbProperties dbProperties = DbProperties.getInstance();
        dbProperties.init("dev");

        Assertions.assertEquals("10", dbProperties.getValue(DbProperties.Key.MAX_POOL));
        Assertions.assertEquals("bookie_dev", dbProperties.getValue(DbProperties.Key.USER));

    }
}