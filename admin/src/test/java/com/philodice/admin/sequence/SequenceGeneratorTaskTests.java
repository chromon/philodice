package com.philodice.admin.sequence;

import com.philodice.admin.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.concurrent.*;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SequenceGeneratorTaskTests {

    private final RedisService redisService;

    public SequenceGeneratorTaskTests(RedisService redisService) {
        this.redisService = redisService;
    }

    @Test
    public void testTask2() {

        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        long totalIds = 100_000_000; // 一亿个ID
        long idsPerThread = totalIds / numThreads;

        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>((int) totalIds);

        SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            executorService.execute(() -> {
                for (long j = 0; j < idsPerThread; j++) {
                    queue.add(Long.toString(idGenerator.nextId()));
                }
                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

        long endTime = System.currentTimeMillis();

        System.out.println("Total time: " + (endTime - startTime) + " ms");
        System.out.println("ID generation completed.");
    }

    @Test
    public void test3() throws InterruptedException {
//        int TARGET_ID_COUNT = 100_000_000;
        int TARGET_ID_COUNT = 10_000_000;
        int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        SnowflakeIdGenerator idGenerator = SnowflakeIdGenerator.getInstance();

        String key = "ids";

        long startTime = System.currentTimeMillis();
        System.out.println("start...");

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    long startTime2 = System.currentTimeMillis();

                    int count = 0;
                    BlockingQueue<Long> queue = new LinkedBlockingQueue<>();
                    while (count < TARGET_ID_COUNT / (THREAD_COUNT)) {
                        queue.add(idGenerator.nextId());
                        count++;
                    }

                    redisService.add(key, queue.toArray());

                    long endTime2 = System.currentTimeMillis();
                    System.out.println("Total time 2: " + (endTime2 - startTime2) + " ms");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();

        System.out.println("Total time: " + (endTime - startTime) + " ms");
        System.out.println("ID generation completed.");
    }
}
