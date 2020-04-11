/*
 * CLHTest.java
 * JUnit based test
 *
 * Created on January 12, 2006, 8:27 PM
 */

package org.artofmp.chapter7.spin;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Crude & inadequate test of lock class.
 *
 * @author Maurice Herlihy
 */
public class CLHLockTest {
    private final static int THREADS = 8;
    private final static int COUNT = 8 * 1024;
    private final static int PER_THREAD = COUNT / THREADS;
    Thread[] thread = new Thread[THREADS];
    int counter = 0;

    CLHLock instance = new CLHLock();

    @Test
    public void testParallel() throws Exception {
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new MyThread();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].join();
        }

        assertEquals(counter, COUNT);
    }

    class MyThread extends Thread {
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                instance.lock();
                try {
                    counter = counter + 1;
                } finally {
                    instance.unlock();
                }
            }
        }
    }
}
