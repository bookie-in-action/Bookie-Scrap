package com.bookie.scrap.common.util;

import com.bookie.scrap.common.properties.BookieProperties;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;

public class NewSnowflakeIdGenerator {

    /**
    * timestamp (41) + serverId (10) + sequence (12)
    */

    private static final long EPOCH = LocalDateTime.of(2025, 3, 17, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static final long SERVER_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + SERVER_ID_BITS;

    private static final long MAX_SERVER_ID = 1023; // 10비트 (2^10 - 1)
    private static final long MAX_SEQUENCE = 4095; // 12비트 (2^12 - 1)

    private static long serverId;
    private static final AtomicLong sequence = new AtomicLong();
    private static final AtomicLong lastTimestamp = new AtomicLong();

    static {
        int loadedServerId = Integer.parseInt(
                BookieProperties.getInstance().getValue(BookieProperties.Key.SERVER_ID)
        );

        if (loadedServerId < 0 || loadedServerId > MAX_SERVER_ID) {
            throw new IllegalArgumentException("serverId out of range: 0-" + MAX_SERVER_ID);
        }
        NewSnowflakeIdGenerator.serverId = loadedServerId;
    }

    public static String getId() {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis < lastTimestamp.get()) {
            currentTimeMillis = waitUntilCurrentIsLargerThanLast(currentTimeMillis);
            sequence.set(0);
        }

        if (currentTimeMillis == lastTimestamp.get()) { // 같은 밀리초에서 id 생성하는 경우
            long nextSeq = sequence.incrementAndGet();

            if (nextSeq > MAX_SEQUENCE) { // 같은 밀리초에서 sequence를 다 사용한 경우
                currentTimeMillis = waitNextMillis(currentTimeMillis);
                sequence.set(0);
            }
        } else { // 새로운 밀리초 시작된 경우
            sequence.set(0);
        }

        long prevTimestamp;
        do {
            prevTimestamp = lastTimestamp.get();
        } while (!lastTimestamp.compareAndSet(prevTimestamp, currentTimeMillis));

        long id = ((currentTimeMillis - EPOCH) << TIMESTAMP_SHIFT) | (serverId << SEQUENCE_BITS) | sequence.get();

        return Long.toString(id);
    }

    private static long waitNextMillis(long currentTimeMillis) {
        int spinCount = 0;
        long nextTimestampMillis = System.currentTimeMillis();

        if (nextTimestampMillis > currentTimeMillis) {
            return nextTimestampMillis;
        }

        do {
            if (spinCount < 100) { // 100번까지는 빠르게 Spin-Wait
                Thread.onSpinWait();
            } else {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            spinCount++;
            nextTimestampMillis = System.currentTimeMillis();
        } while (nextTimestampMillis <= currentTimeMillis);

        return nextTimestampMillis;
    }

    private static long waitUntilCurrentIsLargerThanLast(long currentTimeMillis) {
        int spinCount = 0;

        long sleepTime = lastTimestamp.get() - currentTimeMillis - 1;
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        do {
            if (spinCount < 100) { // 100번까지는 빠르게 Spin-Wait
                Thread.onSpinWait();
            } else {
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            spinCount++;
            currentTimeMillis = System.currentTimeMillis();
        } while (lastTimestamp.get() >= currentTimeMillis);

        return currentTimeMillis;
    }

}
