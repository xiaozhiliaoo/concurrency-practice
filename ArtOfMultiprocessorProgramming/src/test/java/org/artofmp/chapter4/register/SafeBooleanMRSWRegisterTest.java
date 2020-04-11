package org.artofmp.chapter4.register;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author lili
 * @date 2020/4/11 14:38
 * @description
 * @notes
 */
public class SafeBooleanMRSWRegisterTest {
    private final static int THREADS = 8;
    Thread[] thread = new Thread[THREADS];
    SafeBooleanMRSWRegister instance = new SafeBooleanMRSWRegister(THREADS);


    @Test
    public void testSequential() {
        System.out.println("sequential read and write");
        instance.write(true);
        boolean result = instance.read();
        assertEquals(result, true);
    }


    /**
     * Parallel reads
     */
    @Test
    public void testParallel() throws Exception {
        instance.write(false);
        instance.write(true);
        System.out.println("parallel read");
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new ReadThread();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].join();
        }
    }
    class ReadThread extends Thread {
        public void run() {
            if (!instance.read()) {
                fail("register returns false");
            }
        }
    }



}