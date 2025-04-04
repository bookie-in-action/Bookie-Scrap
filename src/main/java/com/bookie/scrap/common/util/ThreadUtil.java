package com.bookie.scrap.common.util;

import com.bookie.scrap.common.properties.BookieProperties;

import static com.bookie.scrap.common.properties.BookieProperties.Key.*;

public class ThreadUtil {

    public static Long sleepMillis = Long.parseLong(BookieProperties.getInstance().getValue(THREAD_SLEEP));

    public static void sleep() {
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
