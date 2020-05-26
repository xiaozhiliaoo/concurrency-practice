/*
 * TreeTest.java
 * JUnit based test
 *
 * Created on June 16, 2006, 9:32 PM
 */

package org.artofmp.chapter12.combine;

import junit.framework.*;

import java.util.Stack;

/**
 * @author mph
 */
public class TreeTest extends TestCase {

    final static int THREADS = 8;
    final static int TRIES = 1024 * 1024;
    static boolean[] test = new boolean[THREADS * TRIES];
    Tree instance = new Tree(THREADS);
    Thread[] thread = new Thread[THREADS];

    public TreeTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TreeTest.class);

        return suite;
    }

    public void testGetAndIncrement() throws Exception {
        System.out.printf("Parallel, %d threads, %d tries\n", THREADS, TRIES);
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new MyThread();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].join();
        }
        check(test);
    }


    class MyThread extends Thread {
        public void run() {
            try {
                for (int j = 0; j < TRIES; j++) {
                    int i = instance.getAndIncrement();
                    if (test[i]) {
                        System.out.printf("ERROR duplicate value %d\n", i);
                    } else {
                        test[i] = true;
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

    void check(boolean[] test) throws Exception {
        for (int i = 0; i < test.length; i++) {
            if (!test[i]) {
                System.out.println("missing value at " + i);
                throw new Exception();
            }
        }
    }

}
