package com.philodice.admin.sequence;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SnowflakeIdGenerator {
    // 开始时间戳 (2022-01-01 00:00:00)
    private static final long TW_EPOCH = 1640995200000L;
    // 机器 id 所占的位数
    private static final long WORKER_ID_BITS = 5L;
    // 数据中心 id 所占的位数
    private static final long DATA_CENTER_ID_BITS = 5L;
    // 支持的最大机器 id，-1L ^ (-1L << WORKER_ID_BITS) 的简化形式
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 支持的最大数据中心 id
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);
    // 序列号所占的位数
    private static final long SEQUENCE_BITS = 12L;
    // 机器 id 的偏移量
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心 id 的偏移量
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳的偏移量
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    // 序列号的掩码
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private final long workerId;
    private final long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private static SnowflakeIdGenerator instance;
    // 随机生成一个机器 id
    private static final long WORKER_ID = ThreadLocalRandom.current().nextLong(0, MAX_WORKER_ID + 1);
    // 随机生成一个数据中心 id
    private static final long DATA_CENTER_ID = ThreadLocalRandom.current().nextLong(0, MAX_DATA_CENTER_ID + 1);

    private SnowflakeIdGenerator() {
        this.workerId = WORKER_ID;
        this.dataCenterId = DATA_CENTER_ID;
    }

    public static synchronized SnowflakeIdGenerator getInstance() {
        if (instance == null) {
            instance = new SnowflakeIdGenerator();
        }
        return instance;
    }

    public synchronized long nextId() {
        long currTimestamp = System.currentTimeMillis();
        if (currTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.");
        }
        if (currTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                currTimestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = currTimestamp;
        return ((currTimestamp - TW_EPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
