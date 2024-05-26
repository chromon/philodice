package com.philodice.admin.sequence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SequencePoolTests {

    private final SequencePool sequencePool;

    public SequencePoolTests(SequencePool sequencePool) {
        this.sequencePool = sequencePool;
    }

    @Test
    public void testInitSequence() {
        try {
            Long number = sequencePool.initPool();
            System.out.println(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddSequence() {
        try {
            Long number = sequencePool.addSequences(1000002L);
            System.out.println(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetSequence() {
        System.out.println(sequencePool.getSequence());
    }

    @Test
    public void testGetInfo() {
        System.out.println(sequencePool.getStatus());
    }
}
