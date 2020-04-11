package org.artofmp.chapter2;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author lili
 * @date 2020/4/11 13:11
 * @description
 * @notes
 */
public class PetersonTest {


    private final static int THREADS = 2;
    private final static int COUNT = 1024;
    private final static int PER_THREAD = COUNT / THREADS;
    Thread[] thread = new Thread[THREADS];
    int counter = 0;

    @Test
    public void testParallel() throws InterruptedException {
        System.out.println("parallel");
        ThreadID.reset();
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


    Peterson instance = new Peterson();


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