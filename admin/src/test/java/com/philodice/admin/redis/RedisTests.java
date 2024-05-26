package com.philodice.admin.redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class RedisTests {

    private final RedisService redisService;

    public RedisTests(RedisService redisService) {
        this.redisService = redisService;
    }

    @Test
    public void testRedisSet() {
        redisService.add("fruits", "apple", "banana", "orange");
        String fruits = (String) redisService.pop("fruits");
        System.out.println(fruits);
    }
}
