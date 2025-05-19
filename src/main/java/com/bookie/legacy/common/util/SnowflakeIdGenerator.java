package com.bookie.legacy.common.util;

import com.bookie.legacy.common.domain.snowflake.SystemTimeProvider;
import com.bookie.legacy.common.domain.snowflake.TimeProvider;
import com.bookie.legacy.common.properties.BookieProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class SnowflakeIdGenerator {

    /**
    * timestamp (41) + serverId (10) + sequence (12)
    */

    private static final long EPOCH = LocalDateTime.of(2025, 3, 17, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
    private static final long SERVER_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_SERVER_ID = (1L << SERVER_ID_BITS) - 1; // 10비트 (2^10 - 1)
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 12비트 (2^12 - 1)

    private static long serverId;
    private static long sequence = 0;
    private static long lastTimestamp = 0;

    @Setter
    private static TimeProvider timeProvider = new SystemTimeProvider();

    static {
        int loadedServerId = Integer.parseInt(
                BookieProperties.getInstance().getValue(BookieProperties.Key.SERVER_ID)
        );

        if (loadedServerId < 0 || loadedServerId > MAX_SERVER_ID) {
            throw new IllegalArgumentException("serverId have to be in range: 0-" + MAX_SERVER_ID);
        }
        SnowflakeIdGenerator.serverId = loadedServerId;
    }

    public static synchronized String getId() {
        long currentTimeMillis = timeProvider.currentTimeMillis();

        if (currentTimeMillis < lastTimestamp) {
            currentTimeMillis = waitUntilCurrentIsLargerThanLast(currentTimeMillis);
            sequence = 0;
        }

        if (currentTimeMillis == lastTimestamp) { // 같은 밀리초에서 id 생성하는 경우
            long nextSeq = ++sequence;

            if (nextSeq > MAX_SEQUENCE) { // 같은 밀리초에서 sequence를 다 사용한 경우
                currentTimeMillis = waitNextMillis(currentTimeMillis);
                sequence = 0;
            }
        } else { // 새로운 밀리초 시작된 경우
            sequence = 0;
        }

        long id;
        lastTimestamp = currentTimeMillis;

        id = ((currentTimeMillis - EPOCH) << SEQUENCE_BITS + SERVER_ID_BITS) |
                (serverId << SEQUENCE_BITS) |
                sequence;

        return Long.toString(id);
    }

    private static long waitNextMillis(long currentTimeMillis) {
        int spinCount = 0;
        long nextTimestampMillis = timeProvider.currentTimeMillis();

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
            nextTimestampMillis = timeProvider.currentTimeMillis();
        } while (nextTimestampMillis <= currentTimeMillis);

        return nextTimestampMillis;
    }

    private static long waitUntilCurrentIsLargerThanLast(long currentTimeMillis) {
        int spinCount = 0;

        log.debug("lastTimeStamp: {}, currentTimeMillis: {}", lastTimestamp, currentTimeMillis);

        long sleepTime = lastTimestamp - currentTimeMillis - 1;
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
            currentTimeMillis = timeProvider.currentTimeMillis();
        } while (lastTimestamp >= currentTimeMillis);

        return currentTimeMillis;
    }

}
