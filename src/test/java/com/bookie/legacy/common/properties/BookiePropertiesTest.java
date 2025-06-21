package com.bookie.legacy.common.properties;

import com.bookie.legacy.common.properties.BookieProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
@Disabled
class BookiePropertiesTest {

    static BookieProperties bookieProperies;

    @BeforeAll
    static void init() {
        bookieProperies = BookieProperties.getInstance();
        bookieProperies.init("dev");
    }

    @Test
    void retry_count() {
        bookieProperies.init("dev");

        Assertions.assertEquals("3", bookieProperies.getValue(BookieProperties.Key.RETRY_COUNT));
    }

    @Test
    void server_id() {
        BookieProperties bookieProperies = BookieProperties.getInstance();
        bookieProperies.init("dev");

        Assertions.assertEquals("1", bookieProperies.getValue(BookieProperties.Key.SERVER_ID));
    }

    @Test
    void thread_sleep() {
        BookieProperties bookieProperies = BookieProperties.getInstance();
        bookieProperies.init("dev");

        Assertions.assertEquals("500", bookieProperies.getValue(BookieProperties.Key.THREAD_SLEEP));
    }

}