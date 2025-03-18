package com.bookie.scrap.common.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookiePropertiesTest {

    @Test
    void init() {
        BookieProperties bookieProperies = BookieProperties.getInstance();
        bookieProperies.init("dev");

        Assertions.assertEquals("3", bookieProperies.getValue(BookieProperties.Key.RETRY_COUNT));
        Assertions.assertEquals("1", bookieProperies.getValue(BookieProperties.Key.SERVER_ID));
    }

}