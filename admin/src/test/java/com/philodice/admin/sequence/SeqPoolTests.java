package com.philodice.admin.sequence;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SeqPoolTests {

    private final SeqPool seqPool;

    public SeqPoolTests(SeqPool seqPool) {
        this.seqPool = seqPool;
    }

    @Test
    public void testInitSequence() {
        try {
            Long number = seqPool.initPool();
            System.out.println(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAddSequence() {
        try {
            Long number = seqPool.addSequences(1000002L);
            System.out.println(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetSequence() {
        System.out.println(seqPool.getSequence());
    }

    @Test
    public void testGetInfo() {
        System.out.println(seqPool.getStatus());
    }
}
