package com.bookie.legacy.common.domain.snowflake;

public class FakeTimeProvider implements TimeProvider{

    long timeMillis;
    long lastTimestamp;
    boolean isRunning;

    public FakeTimeProvider() {
        this.timeMillis = System.currentTimeMillis();
        this.isRunning = false;
        this. lastTimestamp = 0L;
    }

    @Override
    public long currentTimeMillis() {
        if (isRunning) {
            long elapsedTime = System.currentTimeMillis() - lastTimestamp;
            return timeMillis + elapsedTime;
        }

        return timeMillis;
    }

    public void startRunning() {
        this.isRunning = true;
        this.lastTimestamp = System.currentTimeMillis();
    }

    public void stopRunning() {
        this.isRunning = false;
    }

    public void goFuture(int millis) {
        this.timeMillis += millis;
    }

    public void goPast(int millis) {
        this.timeMillis -= millis;
    }

}
