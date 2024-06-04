package com.philodice.core.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * Redis 工具类
 */
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 移除并返回 set 集合中的一个随机元素
     * @param key set 集合 key 值
     * @return 返回的元素
     */
    public Object popValue(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 检查指定的 hash 表中是否存在以 field 为键的值
     *
     * @param key hash 表键值
     * @param field 待检测的 field 键
     * @return 是否存在对应值
     */
    public boolean hashExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 在 hash 表中设置指定 field 键与 value 值
     *
     * @param key  hash 表键值
     * @param field field 键
     * @param value String 类型 value 值
     */
    public void hashSet(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 在 hash 表中设置指定 field 键与 value 值
     *
     * @param key  hash 表键值
     * @param field field 键
     * @param value long 类型 value 值
     */
    public void hashSet(String key, String field, long value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 从 hash 表中获取指定 field 键对应的值
     *
     * @param key  hash 表键值
     * @param field field 键
     * @return hash value
     */
    public Object hashGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 执行 HINCRBY key field value 命令
     * 将哈希字段 field 对应的值增加 value。如果该字段不存在,则会先初始化为 0,再执行增量操作。
     *
     * @param key hash 表键值
     * @param field field 键
     * @param value 增量
     */
    public void hashIncreby(String key, String field, long value) {
        redisTemplate.opsForHash().increment(key, field, value);
    }

    /**
     * 删除 hash 表中指定 field 键对应的值
     *
     * @param key hash 表键值
     * @param fields 一到多个 field 键
     * @return Number of deletions
     */
    public Long hashDel(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 将 map 集合中的键值对添加到指定 key 的 hash 表中
     *
     * @param key hash 表键值
     * @param map 待添加 map 集合
     */
    public void setHashEntries(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 根据指定的 key 获取 hash 表所有的键值对集合
     *
     * @param key hash 表键值
     * @return 键值对集合
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取 hash 表的大小
     *
     * @param key hash 表键值
     * @return hash 表的大小
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }
}
