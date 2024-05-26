package com.philodice.admin.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向集合添加一个或多个成员
     * @param key 集合 key 值
     * @param values 添加的元素
     * @return 添加的元素数量
     */
    public Long add(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 从集合中移除元素
     * @param key 集合 key 值
     * @param values 待删除的元素
     * @return 删除的元素数量
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 移除并返回集合中的一个随机元素
     * @param key 集合 key 值
     * @return 返回的元素
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 检查元素是否存在于集合
     * @param key 集合 key 值
     * @param value 待检查的元素
     * @return 是否存在于集合中
     */
    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取集合的大小
     * @param key 集合 key 值
     * @return 集合的大小
     */
    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 获取集合中的所有元素
     * @param key 集合 key 值
     * @return 集合中的所有元素
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 为集合设置过期时间
     * @param key 集合 key 值
     * @param timeout 过期时间
     * @param unit 过期时间单位
     * @return 设置是否成功
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }
}
