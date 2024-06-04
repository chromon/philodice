package com.philodice.core.link;

import com.philodice.core.Entity.SeqPoolEntity;
import com.philodice.core.redis.RedisService;
import org.springframework.stereotype.Service;

@Service
public class SeqPoolService {

    private final RedisService redisService;

    private final SeqPoolEntity seqPoolEntity;

    public SeqPoolService(RedisService redisService, SeqPoolEntity seqPoolEntity) {
        this.redisService = redisService;
        this.seqPoolEntity = seqPoolEntity;
    }

    /**
     * 从序列号池中弹出一个序列号
     * @return 序列号
     */
    public Object popSequence() {
        return redisService.popValue(seqPoolEntity.getPrefix());
    }
}
