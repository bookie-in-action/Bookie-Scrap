package com.bookie.scrap.common.domain.snowflake;

public class SystemTimeProvider implements TimeProvider{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
