package com.philodice.admin.log;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTests {

    @Test
    public void testLogger() {
        Logger logger = LoggerFactory.getLogger(LogTests.class);
        logger.info("hah");
    }
}
