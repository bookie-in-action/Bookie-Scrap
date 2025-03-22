package com.bookie.scrap.common.util;

import com.bookie.scrap.common.domain.util.FakeTimeProvider;
import com.bookie.scrap.common.domain.util.SystemTimeProvider;
import com.bookie.scrap.common.lifecycle.InitManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class SnowflakeIdGeneratorTest {

    private final long SEQUENCE_BITS = 12L;

    private final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1; // 12비트 (2^12 - 1)

    @BeforeAll
    static void before() {
        new InitManager().devInit();
    }

    @AfterEach
    void after() {
        SnowflakeIdGenerator.setTimeProvider(new SystemTimeProvider());
    }

    @Test
    @DisplayName("(FakeTimeProvider)시간 역행 테스트")
    void testClockBackwardHandling(){

        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider();
        fakeTimeProvider.startRunning();
        SnowflakeIdGenerator.setTimeProvider(fakeTimeProvider);

        String pastId = SnowflakeIdGenerator.getId();
        log.info("pastID {}", pastId);

        fakeTimeProvider.goPast(5);

        String lastId = SnowflakeIdGenerator.getId();
        log.info("lastID {}", lastId);

        // ID는 절대 감소하면 안 됨
        Assertions.assertTrue(Long.parseLong(pastId) < Long.parseLong(lastId));
    }

    @Test
    @DisplayName("(FakeTimeProvider) 같은 밀리초 내에서 시퀀스는 max가 4095, 그 이후 0")
    void testSequenceIncreaseWithinSameMillisecond() {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider();
        SnowflakeIdGenerator.setTimeProvider(fakeTimeProvider);

        // FakeTimeProvider이기 때문에 4095까지 생성하고 계속 jvm이 돌아가야 함
        long extractedSequence = -1L;
        for (int i = 0; i <= MAX_SEQUENCE + 1; i++) {

            // 시퀀스가 max에 도달하면 스레드가 계속 기다리기에 그 전에 다른 스레드로 wait하다가 1밀리초를 늘려줌
            if (i == MAX_SEQUENCE - 1) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        fakeTimeProvider.goFuture(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }

            long nextId = Long.parseLong(SnowflakeIdGenerator.getId());
            extractedSequence = nextId & MAX_SEQUENCE;
            log.info("Extracted sequence {}, id {}", extractedSequence, nextId);
        }

        Assertions.assertTrue(extractedSequence == 0);
    }

    @Test
    @DisplayName("(SystemTimeProvider) 시퀀스 range 테스트, 성능 테스트")
    void testSequenceOverflow() {
        long maxSequence = 0;

        long extractedSequence = -1L;
        for (int i = 0; i <= MAX_SEQUENCE * 2; i++) {
            long nextId = Long.parseLong(SnowflakeIdGenerator.getId());
            extractedSequence = nextId & MAX_SEQUENCE;
            maxSequence = Math.max(extractedSequence, maxSequence);
            log.info("Extracted sequence {}, id {}", extractedSequence, nextId);
        }

        log.info("최대 시퀀스 {}", maxSequence);

        assertTrue(extractedSequence <= MAX_SEQUENCE);
        assertTrue(extractedSequence >= 0);

    }


    @Test
    @DisplayName("(FakeTimeProvider) 시간 역행 시 기다리는 시간 확인")
    void testWaitNextMillis() {

        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider();
        SnowflakeIdGenerator.setTimeProvider(fakeTimeProvider);
        fakeTimeProvider.startRunning();

        SnowflakeIdGenerator.getId();
        long before = System.currentTimeMillis();
        fakeTimeProvider.goPast(10);
        SnowflakeIdGenerator.getId();
        long after = System.currentTimeMillis();

        log.info("before {}, after {}, diff {}", before, after, after-before);
        assertTrue(after >= before + 10, "waitNextMillis()가 최소 10ms 이상 기다려야 함");
    }

    @Test
    @DisplayName("(SystemTimeProvider) 멀티 스레드 환경 테스트")
    void testMultiThreadedIdGeneration() throws InterruptedException, ExecutionException {
        int threadCount = 10;
        int idCountPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Set<String> uniqueIds = ConcurrentHashMap.newKeySet(threadCount * idCountPerThread);

        List<Callable<Void>> callableTasks = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for (int i = 0; i < threadCount; i++) {
            callableTasks.add(() -> {
                log.info("스레드 시작 (카운트다운 전)");
                countDownLatch.await();
                log.info("스레드 시작 (카운트다운 끝)");
                for (int j = 0; j < idCountPerThread; j++) {
                    uniqueIds.add(SnowflakeIdGenerator.getId());
                }
                return null;
            });
        }

        List<Future<Void>> futures = new ArrayList<>();
        for (Callable<Void> callableTask : callableTasks) {
            futures.add(executor.submit(callableTask));
        }

        countDownLatch.countDown();

        for (Future<Void> future : futures) {
            future.get();
        }

        executor.shutdown();
        while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {}

        assertEquals(threadCount * idCountPerThread, uniqueIds.size(), "멀티스레드에서도 ID가 중복 없이 생성되어야 함");
    }

    @Test
    void testSingleThreadedLargeIdCount() {
        Set<String> uniqueIds = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            uniqueIds.add(SnowflakeIdGenerator.getId());
        }

        assertEquals(10000, uniqueIds.size());
    }


}