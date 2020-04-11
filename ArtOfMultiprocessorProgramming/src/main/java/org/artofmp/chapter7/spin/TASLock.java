/*
 * TASLock.java
 *
 * Created on January 20, 2006, 10:48 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter7.spin;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Test-and-set lock.
 *
 * @author Maurice Herlihy
 */
public class TASLock implements Lock {
    AtomicBoolean state = new AtomicBoolean(false);

    public void lock() {
        while (state.getAndSet(true)) {
        } // spin
    }

    public void unlock() {
        state.set(false);
    }

    // Any class that implents Lock must provide these methods.
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
