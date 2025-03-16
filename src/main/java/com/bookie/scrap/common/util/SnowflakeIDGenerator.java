package com.bookie.scrap.common.util;

/**
 * SnowflakeIDGenerator는 트위터의 Snowflake 알고리즘을 기반으로 한 고유한 64비트 ID 생성기
 *
 * - 이 클래스는 특정 데이터센터와 워커 ID를 기반으로 고유한 ID를 생성합니다.
 * - ID는 타임스탬프, 데이터센터 ID, 워커 ID 및 시퀀스 번호로 구성됩니다.
 * - 이 클래스는 싱글톤 패턴을 적용하여 단일 인스턴스를 사용하도록 보장합니다.
 *
 * 비트 구성
 * - 1비트    : 부호 비트 (항상 0)
 * - 41비트   : 타임스탬프 (EPOCH 기준 경과 시간)
 * - 5비트    : 데이터센터 ID
 * - 5비트    : 워커 ID
 * - 12비트   : 시퀀스 번호
 *
 * @author Yeoonnii
 * @version 1.0
 */
public class SnowflakeIDGenerator {

    // 기준 타임스탬프 (EPOCH): 2025-01-01 00:00:00 UTC
    private static final long EPOCH = 1735689600000L; // 2025-01-01 00:00:00 UTC 기준

    private static final long WORKER_ID_BITS = 5L;      // 데이터센터 ID 비트 수 (5비트)
    private static final long DATACENTER_ID_BITS = 5L;  // 워커 ID 비트 수 (5비트)
    private static final long SEQUENCE_BITS = 12L;      // 시퀀스 비트 수 (12비트)

    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);         // 데이터센터 ID의 최대값 (31)
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS); // 워커 ID의 최대값 (31)
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);           // 시퀀스의 최대값 (4095)

    private final long datacenterId;    // 데이터센터 ID
    private final long workerId;        // 워커 ID
    private long sequence = 0L;         // 시퀀스 번호 (초기값 0)
    private long lastTimestamp = -1L;   // 마지막으로 ID를 생성한 타임스탬프 (초기값 -1)

    // 싱글톤 인스턴스
    private static volatile SnowflakeIDGenerator instance;

    /**
     * SnowflakeIDGenerator의 생성자
     * 데이터센터 ID와 워커 ID가 범위를 벗어나면 예외 발생
     *
     * @param datacenterId 데이터센터 ID (0~31)
     * @param workerId 워커 ID (0~31)
     * @throws IllegalArgumentException 유효하지 않은 데이터센터 ID 또는 워커 ID가 입력될 경우
     */
    private SnowflakeIDGenerator(long datacenterId, long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID out of range");
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID out of range");
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * SnowflakeIDGenerator의 싱글톤 인스턴스를 반환
     *
     * @param datacenterId 데이터센터 ID
     * @param workerId 워커 ID
     * @return SnowflakeIDGenerator의 인스턴스
     */
    public static SnowflakeIDGenerator getInstance(long datacenterId, long workerId) {
        if (instance == null) {
            synchronized (SnowflakeIDGenerator.class) {
                if (instance == null) {
                    instance = new SnowflakeIDGenerator(datacenterId, workerId);
                }
            }
        }
        return instance;
    }

    /**
     * 새로운 Snowflake ID를 생성
     *
     * 시퀀스 번호는 동일한 밀리초 내에서 1씩 증가하며, 최대값(4095)에 도달하면 다음 밀리초가 될 때까지 대기한 후 새로운 ID를 생성
     *
     * @return 생성된 64비트 ID
     * @throws RuntimeException 시스템 시간이 뒤로 이동한 경우 중복 ID 생성 방지를 위해 예외 발생
     */
    public synchronized long nextId() {
        long timestamp = currentTimestamp();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << (WORKER_ID_BITS + DATACENTER_ID_BITS + SEQUENCE_BITS))
                | (datacenterId << (WORKER_ID_BITS + SEQUENCE_BITS))
                | (workerId << SEQUENCE_BITS)
                | sequence;
    }

    /**
     * 현재 타임스탬프가 증가할 때까지 대기
     *
     * @param lastTimestamp 이전 타임스탬프
     * @return 증가한 타임스탬프
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = currentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }
        return timestamp;
    }

    /**
     * 현재 시간을 밀리초 단위로 반환
     *
     * @return 현재 타임스탬프 (밀리초)
     */
    private long currentTimestamp() {
        return System.currentTimeMillis();
    }
}
