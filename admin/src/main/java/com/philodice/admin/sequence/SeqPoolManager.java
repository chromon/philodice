package com.philodice.admin.sequence;

import org.springframework.stereotype.Service;

/**
 * 序列号管理对外接口
 */
@Service
public class SeqPoolManager {

    private final SeqPool pool;

    public SeqPoolManager(SeqPool pool) {
        this.pool = pool;
    }

    /**
     * 初始化序列号池
     */
    public Long initPool() throws InterruptedException {
        return pool.initPool();
    }

    /**
     * 添加指定数量的序列号
     * @param number 序列号数量
     */
    public Long addSequences(Long number) throws InterruptedException {
        return pool.addSequences(number);
    }

    /**
     * 序列号池相关信息
     * @return 信息字符串
     */
    public String getStatus() {
        return pool.getStatus();
    }
}
