package com.mmall.practice.example.count;

import com.mmall.practice.annoations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@NotThreadSafe
public class CountExample1 {

    private static int threadTotal = 200;
    private static int clientTotal = 5000;

    private static long count = 0;

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        final Semaphore semp = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int index = 0; index < clientTotal; index++) {
            exec.execute(() -> {
                try {
                    semp.acquire();
                    add();
                    semp.release();
                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        exec.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count++;
    }
}
