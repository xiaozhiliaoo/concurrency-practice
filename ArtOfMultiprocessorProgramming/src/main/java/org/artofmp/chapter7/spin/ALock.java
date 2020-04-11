/*
 * ALock.java
 *
 * Created on January 20, 2006, 11:02 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter7.spin;

/**
 * Anderson lock
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.ThreadLocal;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class ALock implements Lock {
    // thread-local variable
    ThreadLocal<Integer> mySlotIndex = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    AtomicInteger tail;
    boolean[] flag;
    int size;

    /**
     * Constructor
     * @param capacity max number of array slots
     */
    public ALock(int capacity) {
        size = capacity;
        tail = new AtomicInteger(0);
        flag = new boolean[capacity];
        flag[0] = true;
    }

    public void lock() {
        int slot = tail.getAndIncrement() % size;
        mySlotIndex.set(slot);
        while (!flag[mySlotIndex.get()]) {
        }
        ; // spin
    }

    public void unlock() {
        flag[mySlotIndex.get()] = false;
        flag[(mySlotIndex.get() + 1) % size] = true;
    }

    // any class implementing Lock must provide these methods
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

