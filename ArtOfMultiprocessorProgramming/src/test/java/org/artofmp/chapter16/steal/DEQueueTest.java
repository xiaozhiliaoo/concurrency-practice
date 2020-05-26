/*
 * DEQueueImplTest.java
 * JUnit based test
 *
 * Created on April 7, 2006, 7:07 PM
 */

package org.artofmp.chapter16.steal;

import junit.framework.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

public class DEQueueTest extends TestCase {
    private final static int THREADS = 16;
    private final static int REPEAT = 32;
    int index;
    DEQueue instance;
    boolean[] map = new boolean[THREADS];
    Thread[] thread = new Thread[THREADS];
    Dummy[] input = new Dummy[THREADS];

    public DEQueueTest(String testName) {
        super(testName);
        instance = new DEQueue(THREADS);
        for (int i = 0; i < THREADS; i++) {
            input[i] = new Dummy(i);
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(DEQueueTest.class);
        return suite;
    }

    /**
     * Sequential calls.
     */
    public void testSequential() {
        System.out.println("sequential pushBottom and popBottom");
        for (int r = 0; r < REPEAT; r++) {
            instance.reset();
            for (int i = 0; i < map.length; i++)
                map[i] = false;
            try {
                for (int i = 0; i < THREADS; i++) {
                    instance.pushBottom(input[i]);
                }
                for (int i = 0; i < THREADS; i++) {
                    Dummy d;
                    if (i % 2 == 0) {
                        d = (Dummy) instance.popTop();
                    } else {
                        d = (Dummy) instance.popBottom();
                    }
                    if (map[d.index]) {
                        fail("duplicate popBottom at index " + d.index);
                    } else {
                        map[d.index] = true;
                    }
                }
            } catch (Exception e) {
                fail("Exception " + e);
            }
            for (int i = 0; i < THREADS; i++) {
                if (!map[i])
                    fail("missing element at index " + i);
            }
        }
    }

    /**
     * Sequential calls.
     */
    public void testConcurrent() {
        System.out.println("concurrent pushBottom and popBottom");
        for (int r = 0; r < REPEAT; r++) {
            instance.reset();
            for (int i = 0; i < map.length; i++)
                map[i] = false;
            Thread[] threads = new Thread[THREADS / 2];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new PopTopThread();
                threads[i].start();
            }

            for (int i = 0; i < THREADS; i++) {
                instance.pushBottom(input[i]);
            }
            for (int i = 0; i < THREADS / 2; i++) {
                Dummy d = (Dummy) instance.popBottom();
                if (map[d.index]) {
                    fail("duplicate popBottom at index " + d.index);
                } else {
                    map[d.index] = true;
                }
            }
            for (int i = 0; i < threads.length; i++) {
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                }
            }
            for (int i = 0; i < THREADS; i++) {
                if (!map[i])
                    fail("missing element at index " + i);
            }
        }
    }

    class Dummy implements Runnable {
        public int index;

        public Dummy(int i) {
            index = i;
        }

        public void run() {
        }
    }

    class PopTopThread extends Thread {
        public void run() {
            Dummy value = null;
            while (value == null) {
                value = (Dummy) instance.popTop();
            }
            if (value != null) {
                if (map[value.index]) {
                    fail("DeqThread: duplicate pop");
                }
                map[value.index] = true;
            }
        }
    }
}