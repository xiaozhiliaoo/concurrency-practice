/*
 * Filter.java
 *
 * Created on January 21, 2006, 9:35 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter2;

/**
 * Peterson lock for multiple threads
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Filter implements Lock {
    static final int IDLE = -1;
    volatile int[] level;
    volatile int[] victim;
    volatile int size;

    public Filter(int threads) {
        size = threads;
        level = new int[threads];
        victim = new int[threads - 1];
    }

    public void lock() {
        int me = ThreadID.get();
        for (int i = 0; i < size - 1; i++) {
            level[me] = i;
            victim[i] = me;
            // spin while conflicts exist
            while (sameOrHigher(me, i) && victim[i] == me) {
            }
            ;
        }
        level[me] = size - 1;
    }

    // Is there another thread at the same or higher level?
    private boolean sameOrHigher(int me, int myLevel) {
        for (int id = 0; id < size; id++)
            if (id != me && level[id] >= myLevel) {
                return true;
            }
        return false;
    }

    public void unlock() {
        int me = ThreadID.get();
        level[me] = IDLE;
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


