package com.philodice.admin.sequence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SnowflakeTests {

    @Test
    public void testGenerator() {
        Snowflake instance = Snowflake.getInstance();
        System.out.println(instance.nextId());
    }
}