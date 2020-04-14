/*
 * Semaphore.java
 *
 * Created on August 23, 2006, 5:49 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Classic Semaphore implementation.
 *
 * @author Maurice Herlihy
 */
public class Semaphore {

    final int capacity;
    int state;
    Lock lock;
    Condition condition;

    public Semaphore(int c) {
        capacity = c;
        state = 0;
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    /**
     * increment semaphore, block if full
     *
     * @throws InterruptedException
     */
    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (state == capacity) {
                condition.await();
            }
            state++;
        } finally {
            lock.unlock();
        }
    }

    /**
     * decrement state.
     */
    public void release() {
        lock.lock();
        try {
            state--;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
