package com.bookie.scrap.common.util;

import com.bookie.scrap.common.lifecycle.InitManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class NewSnowflakeIdGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(NewSnowflakeIdGeneratorTest.class);
    private final long EPOCH = LocalDateTime.of(2025, 3, 18, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
    private final long SERVER_ID_BITS = 10L;
    private final long SEQUENCE_BITS = 12L;
    private final long TIMESTAMP_BITS = 41L;
    private final long TIMESTAMP_SHIFT = SEQUENCE_BITS + SERVER_ID_BITS;

    private final long MAX_SEQUENCE = 4095; // 12비트 (2^12 - 1)

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @Test
    @DisplayName("시간 역행 테스트")
    void testClockBackwardHandling() {
        long fakeCurrentTime = System.currentTimeMillis();
        try (MockedStatic<System> mockedStatic = mockStatic(System.class)) {
            mockedStatic.when(System::currentTimeMillis).thenReturn(fakeCurrentTime);

            NewSnowflakeIdGenerator.getId();
            long lastId = Long.parseLong(NewSnowflakeIdGenerator.getId());

            // 강제로 시간을 역행 (NTP 시간 동기화 문제 시뮬레이션)
            mockedStatic.when(System::currentTimeMillis).thenReturn(fakeCurrentTime - 100);

            long newId = Long.parseLong(NewSnowflakeIdGenerator.getId());

            // ID는 절대 감소하면 안 됨
            assertTrue(newId > lastId, "ID가 역행하지 않아야 한다.");
        }

    }

    // (2) 같은 밀리초에서 많은 ID 생성
    @Test
    void testSequenceIncreaseWithinSameMillisecond() {
        long firstId = Long.parseLong(NewSnowflakeIdGenerator.getId());
        long lastTimestamp = firstId >> (TIMESTAMP_SHIFT);

        // 4095번 호출해서 Sequence가 MAX_SEQUENCE까지 증가해야 함
        for (int i = 0; i <= MAX_SEQUENCE; i++) {
            long newId = Long.parseLong(NewSnowflakeIdGenerator.getId());
            long newTimestamp = newId >> (TIMESTAMP_SHIFT);

            assertEquals(lastTimestamp, newTimestamp, "같은 밀리초 내에서 ID가 생성되어야 함");
        }

        // Sequence가 다 차면 다음 밀리초로 넘어가야 함
        long nextId = Long.parseLong(NewSnowflakeIdGenerator.getId());
        long nextTimestamp = nextId >> (TIMESTAMP_SHIFT);
        assertTrue(nextTimestamp > lastTimestamp, "Sequence가 다 차면 다음 밀리초로 넘어가야 함");
    }

    @Test
    @DisplayName("시퀀스 range 테스트, 성능 테스트")
    void testSequenceOverflow() {
        long maxSequence = 0;

        long extractedSequence = -1L;
        for (int i = 0; i <= MAX_SEQUENCE * 2; i++) {
            long nextId = Long.parseLong(NewSnowflakeIdGenerator.getId());
            extractedSequence = nextId & MAX_SEQUENCE;
            maxSequence = Math.max(extractedSequence, maxSequence);
            log.info("Extracted sequence {}, id {}", extractedSequence, nextId);
        }

        log.info("최대 시퀀스 {}", maxSequence);

        //Sequence 값이 0 ~ 4095 범위 내에 있어야 함
        assertTrue(extractedSequence <= MAX_SEQUENCE);
        assertTrue(extractedSequence >= 0);

    }


    // (4) 서버 ID 범위 초과 테스트
//    @Test
//    @DisplayName("서버 ID 범위 초과 테스트")
//    void testInvalidServerId() {
//        assertThrows(IllegalArgumentException.class, () -> NewSnowflakeIdGeneratorTestHelper.setServerId(2048));
//        assertThrows(IllegalArgumentException.class, () -> NewSnowflakeIdGeneratorTestHelper.setServerId(-1));
//    }

    // (5) waitNextMillis() 동작 확인
//    @Test
//    void testWaitNextMillis() {
//        long before = System.currentTimeMillis();
//        NewSnowflakeIdGeneratorTestHelper.waitNextMillis(before + 10);
//        long after = System.currentTimeMillis();
//
//        assertTrue(after >= before + 10, "waitNextMillis()가 최소 10ms 이상 기다려야 함");
//    }

    // (6) 멀티스레드 환경 테스트
//    @Test
//    void testMultiThreadedIdGeneration() throws InterruptedException, ExecutionException {
//        int threadCount = 10;
//        int idCountPerThread = 1000;
//        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
//        Set<Long> uniqueIds = ConcurrentHashMap.newKeySet();
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        Callable<Void> generateIds = () -> {
//            latch.countDown();
//            latch.await(); // 모든 스레드가 동시에 시작하도록 조정
//            for (int i = 0; i < idCountPerThread; i++) {
//                uniqueIds.add(NewSnowflakeIdGenerator.getId());
//            }
//            return null;
//        };
//
//        for (int i = 0; i < threadCount; i++) {
//            executor.submit(generateIds);
//        }
//
//        executor.shutdown();
//        executor.awaitTermination(10, TimeUnit.SECONDS);
//
//        assertEquals(threadCount * idCountPerThread, uniqueIds.size(), "멀티스레드에서도 ID가 중복 없이 생성되어야 함");
//    }


}