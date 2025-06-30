package com.bookie.legacy.common.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
@Disabled
class ThreadTest {

    @Test
    void onSpinWait() {
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            Thread.onSpinWait();
            long end = System.nanoTime();
            sum += (end - start);
        }

        long avgNs = sum / 100;

        // Thread.onSpinWait() 100회 평균 실행 시간: 65 ns
        // 1밀리초를 위한 Thread.onSpinWait() 실행 횟수: 15384.615384615385
        System.out.println("Thread.onSpinWait() 100회 평균 실행 시간: " + avgNs + " ns");
        System.out.println("1밀리초를 위한 Thread.onSpinWait() 실행 횟수: " + 1_000_000 / (double) avgNs);
    }

    @Test // 다른 스레드에게 실행 기회를 줄 '수도' 있음
    void threadSleep() throws InterruptedException {
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            Thread.sleep(0);
            long end = System.nanoTime();
            sum += (end - start);
        }

        long avgNs = sum / 100;

        /*
        Thread.sleep(0) 100회 평균 실행 시간: 216 ns
        1밀리초를 위한 Thread.sleep(0) 실행 횟수: 4629.62962962963
         */
        System.out.println("Thread.sleep(0) 100회 평균 실행 시간: " + avgNs + " ns");
        System.out.println("1밀리초를 위한 Thread.sleep(0) 실행 횟수: " + 1_000_000 / (double) avgNs);

    }

    @Test // 다른 스레드에게 실행 기회를 주려고 시도
    void threadYield() throws InterruptedException {
        long sum = 0;
        for (int i = 0; i < 100; i++) {
            long start = System.nanoTime();
            Thread.yield();
            long end = System.nanoTime();
            sum += (end - start);
        }

        long avgNs = sum / 100;

        /*
        Thread.sleep(0) 100회 평균 실행 시간: 208 ns
        1밀리초를 위한 Thread.sleep() 실행 횟수: 4807.692307692308
         */
        System.out.println("Thread.yield() 100회 평균 실행 시간: " + avgNs + " ns");
        System.out.println("1밀리초를 위한 Thread.yield() 실행 횟수: " + 1_000_000 / (double) avgNs);

    }

}