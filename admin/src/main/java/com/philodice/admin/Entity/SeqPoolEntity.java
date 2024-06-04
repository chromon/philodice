package com.philodice.admin.Entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 序列号池相关属性实体类
 */
@Component
public class SeqPoolEntity {

    /**
     * 序列号池前缀
     */
    @Value("${seq.pool.prefix}")
    private String prefix;

    /**
     * 序列号池容量
     */
    @Value("${seq.pool.capacity}")
    private Long capacity;

    /**
     * 序列号池扩容阈值，当剩余可用序列号的数量低于这个阈值时，就应该触发扩容操作
     * 值越小，扩容越频繁；值越大，可用序列号数量可能过少。
     */
    @Value("${seq.pool.threshold}")
    private Long threshold;

    /**
     * 序列号池扩容最小增量，防止增量过小
     */
    @Value("${seq.pool.min.increment}")
    private Long minIncrement;

    /**
     * 序列号池增长因子
     */
    @Value("${seq.pool.growth.factor}")
    private Double growthFactor;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getThreshold() {
        return threshold;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public Long getMinIncrement() {
        return minIncrement;
    }

    public void setMinIncrement(Long minIncrement) {
        this.minIncrement = minIncrement;
    }

    public Double getGrowthFactor() {
        return growthFactor;
    }

    public void setGrowthFactor(Double growthFactor) {
        this.growthFactor = growthFactor;
    }
}
