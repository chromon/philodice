package com.philodice.admin.sequence;

import com.philodice.admin.Entity.SequencePoolEntity;
import com.philodice.admin.exception.NotEnoughResourcesException;
import com.philodice.admin.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * 序列号池
 */
@Service
public class SequencePool {

    private final RedisService redisService;

    /**
     * 序列号池前缀
     */
    private final String prefix;

    /**
     * 序列号池容量
     */
    private final Long capacity;

    /**
     * 序列号池临界值
     */
    private final Long threshold;

    /**
     * 序列号池增长因子
     */
    private final Double growthFactor;

    /**
     * 序列号池扩容最小增量
     */
    private final Long minIncrement;

    private final Logger logger;

    public SequencePool(RedisService redisService, SequencePoolEntity sequencePoolEntity) {
        this.redisService = redisService;

        this.prefix = sequencePoolEntity.getPrefix();
        this.capacity = sequencePoolEntity.getCapacity();
        this.threshold = sequencePoolEntity.getThreshold();
        this.growthFactor = sequencePoolEntity.getGrowthFactor();
        this.minIncrement = sequencePoolEntity.getMinIncrement();

        logger = LoggerFactory.getLogger(SequencePool.class);
    }

    /**
     * 初始化序列号池
     * @throws InterruptedException latch.await()
     */
    public Long initPool() throws InterruptedException {
        if (this.getPoolSize() == 0) {
//            return this.expandSequence(this.capacity, "init");
            return this.expandSequence(4000000L, "init");
        }
        return 0L;
    }

    /**
     * 序列号池扩容
     * @param number 扩容数量
     * @param event 扩容事件，包括初始化，扩容等
     * @throws InterruptedException latch.await()
     */
    private Long expandSequence(Long number, String event) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        logger.info("Sequence pool " + event + " starting.");

        // 初始化线程数量
        int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        SnowflakeIdGenerator generator = SnowflakeIdGenerator.getInstance();

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    int count = 0;
                    BlockingQueue<Long> queue = new LinkedBlockingQueue<>();
                    while (count < (number / THREAD_COUNT)) {
                        // 将生成的 id 存入队列中
                        queue.add(generator.nextId());
                        count++;
                    }
                    // 将生成的序列号存入 redis set 中
                    redisService.add(this.prefix, queue.toArray());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        long endTime = System.currentTimeMillis();
        logger.info("Total time: " + (endTime - startTime) + " ms");
        logger.info("Sequence pool " + event + " completed.");

        return number;
    }

    /**
     * 增加指定数量的序列号到序列号池中
     * @param number 指定的增加的序列号数量
     * @throws InterruptedException latch.await()
     */
    public Long addSequences(Long number) throws InterruptedException {
        if (number <= 0) {
            // 未设置 number 时，默认使用容量*增长因子
            number = (long) (this.capacity * this.growthFactor);
        }
        // 增量应应不小于最小增量，防止增量过小
        if (number < this.minIncrement) {
            number = this.minIncrement;
        }
        // 增量应小于剩余容量
        if (this.capacity - this.getPoolSize() < number) {
            number = this.capacity - this.getPoolSize();
        }

        return this.expandSequence(number, "expand");
    }

    /**
     * 从序列号池中获取序列号
     * @return 序列号
     */
    public Long getSequence() {
        long size = this.getPoolSize();
        if (size < 1) {
            throw new NotEnoughResourcesException("Not enough sequence in sequence pool.");
        }
        return (long) redisService.pop(this.prefix);
    }

    /**
     * 获取序列号池大小
     * @return 序列号池大小
     */
     private Long getPoolSize() {
        return redisService.size(this.prefix);
    }

    /**
     * 查询序列号池相关信息
     * 如总容量、剩余序列号数量、临界值、最小增量、增长因子等
     * @return 相关信息
     */
    public String getStatus() {
        return "SequencePool{ " +
                "capacity: " + this.capacity +
                " ,remaining capacity: " + this.getPoolSize() +
                " ,threshold: " + this.threshold +
                ", growth factor: " + this.growthFactor +
                ", expand the capacity? " + (this.getPoolSize() < this.threshold) +
                " }";
    }
}
