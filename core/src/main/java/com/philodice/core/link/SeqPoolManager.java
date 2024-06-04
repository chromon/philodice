package com.philodice.core.link;

import org.springframework.stereotype.Service;

/**
 * 序列号管理类
 */
@Service
public class SeqPoolManager {

    private final SeqPoolService seqPoolService;

    public SeqPoolManager(SeqPoolService seqPoolService) {
        this.seqPoolService = seqPoolService;
    }

    /**
     * 获取一个序列号
     * @return 序列号
     */
    public Long getSeq() {
        return (Long) seqPoolService.popSequence();
    }
}
