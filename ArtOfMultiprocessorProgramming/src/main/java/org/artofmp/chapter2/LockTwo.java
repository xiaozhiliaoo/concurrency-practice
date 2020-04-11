/*
 * LockTwo.java
 *
 * Created on January 21, 2006, 9:31 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter2;

/**
 * Second attempt at a mutual exclusion lock.
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class LockTwo implements Lock {
    private int victim;

    // thread-local index, 0 or 1
    public void lock() {
        int i = ThreadID.get();
        victim = i;                 // let the other go first
        while (victim == i) {
        }      // spin
    }

    public void unlock() {
    }

    // Any class implementing Lock must provide these methods
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock(long time,
                           TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

}

