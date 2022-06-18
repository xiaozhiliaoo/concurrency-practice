package org.concurrency.library.juc;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lili
 * @date 2020/5/30 13:18
 * @description
 * @notes
 */
public class LockTest {

    final Lock lock = new ReentrantLock(true);

    synchronized void intrinsicLock() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                intrinsicLock();
            }
        }, "My thread");
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
        }
    }

    void reentrantLock() {
        lock.lock();
        Thread th = new Thread(new Runnable() {
            public void run() {
                reentrantLock();
            }
        }, "My thread");
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
        }
        lock.unlock();
    }


    public static void main(String[] args) {
        LockTest lockTest = new LockTest();
//        lockTest.intrinsicLock();
        lockTest.reentrantLock();

        //jps  jstack
    }

}
