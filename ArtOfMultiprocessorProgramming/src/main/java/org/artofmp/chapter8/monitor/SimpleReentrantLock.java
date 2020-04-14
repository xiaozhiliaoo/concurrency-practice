/*
 * __NAME__.java
 *
 * Created on __DATE__, __TIME__
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author __USER__
 */
public class SimpleReentrantLock implements Lock {
    Lock lock;
    Condition condition;
    int owner;
    int holdCount;
    private static final int FREE = -1;

    public SimpleReentrantLock() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
        owner = FREE;
        holdCount = 0;
    }

    public void lock() {
        int me = ThreadID.get();
        lock.lock();
        try {
            if (owner == me) {
                holdCount++;
                return;
            }
            while (holdCount != 0) {
                condition.await();
            }
            owner = me;
            holdCount = 1;
        } catch (InterruptedException ex) {
            Logger.getLogger("global").log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unlock() {
        lock.lock();
        try {
            if (holdCount == 0 || owner != ThreadID.get())
                throw new IllegalMonitorStateException();
            holdCount--;
            if (holdCount == 0) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
