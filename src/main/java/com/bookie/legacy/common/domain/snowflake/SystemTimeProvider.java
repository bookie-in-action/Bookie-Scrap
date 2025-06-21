package com.bookie.legacy.common.domain.snowflake;

public class SystemTimeProvider implements TimeProvider{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
