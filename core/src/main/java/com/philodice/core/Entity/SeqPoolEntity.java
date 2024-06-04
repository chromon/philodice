package com.philodice.core.Entity;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
