package com.philodice.admin.sequence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SnowflakeIdGeneratorTests {

    @Test
    public void testGenerator() {
        SnowflakeIdGenerator instance = SnowflakeIdGenerator.getInstance();
        System.out.println(instance.nextId());
    }
}