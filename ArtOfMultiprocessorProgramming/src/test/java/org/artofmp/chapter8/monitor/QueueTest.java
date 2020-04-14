/*
 * QueueTest.java
 * JUnit based test
 *
 * Created on December 27, 2005, 11:15 PM
 */

package org.artofmp.chapter8.monitor;

import junit.framework.*;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.fail;

/**
 * @author Maurice Herlihy
 */
public class QueueTest {
    private final static int THREADS = 8;
    private final static int TEST_SIZE = 64;
    private final static int PER_THREAD = TEST_SIZE / THREADS;
    int index;
    Queue<Integer> instance = new Queue<Integer>(TEST_SIZE);;
    boolean[] map = new boolean[TEST_SIZE];
    Thread[] thread = new Thread[THREADS];


    /**
     * Sequential calls.
     */
    @Test
    public void testSequential() {
        System.out.println("sequential push and pop");

        for (int i = 0; i < TEST_SIZE ; i++) {
            try {
                instance.enq(i);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        for (int i = 0; i < TEST_SIZE; i++) {
            int j = -1;
            try {
                j = (Integer) instance.deq();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (j != i) {
                fail("bad deq: " + j + " expected " + i);
            }
        }
    }

    /**
     * Parallel enqueues, sequential dequeues
     */
    @Test
    public void testParallelEnq() throws Exception {
        System.out.println("parallel enq");
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new EnqThread(i * PER_THREAD);
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].join();
        }
        for (int i = 0; i < TEST_SIZE; i++) {
            int j = instance.deq();
            if (map[j]) {
                fail("duplicate pop: " + j);
            } else {
                map[j] = true;
            }
        }
    }

    /**
     * Sequential enqueues, parallel dequeues
     */
    @Test
    public void testParallelDeq() throws Exception {
        System.out.println("parallel deq");
        for (int i = 0; i < TEST_SIZE; i++) {
            map[i] = false;
        }
        for (int i = 0; i < TEST_SIZE; i++) {
            instance.enq(i);
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new DeqThread();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].join();
        }
    }

    /**
     * Sequential enqueues, parallel dequeues
     */
    @Test
    public void testParallelBoth() throws Exception {
        System.out.println("parallel both");
        Thread[] myThreads = new Thread[2 * THREADS];
        for (int i = 0; i < THREADS; i++) {
            myThreads[i] = new EnqThread(i * PER_THREAD);
            myThreads[i + THREADS] = new DeqThread();
        }
        for (int i = 0; i < 2 * THREADS; i++) {
            myThreads[i].start();
        }
        for (int i = 0; i < 2 * THREADS; i++) {
            myThreads[i].join();
        }
    }

    class EnqThread extends Thread {
        int value;

        EnqThread(int i) {
            value = i;
        }

        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                try {
                    instance.enq(value + i);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    class DeqThread extends Thread {
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                int value = -1;
                try {
                    value = (Integer) instance.deq();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (map[value]) {
                    fail("DeqThread: duplicate pop");
                }
                map[value] = true;
            }
        }
    }

}
