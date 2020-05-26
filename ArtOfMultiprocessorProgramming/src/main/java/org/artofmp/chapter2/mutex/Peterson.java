package org.artofmp.chapter2.mutex;
/*
 * Peterson.java
 *
 * Created on January 20, 2006, 10:36 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * Peterson lock
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.TimeUnit;

public class Peterson implements Lock {
    private boolean[] flag = new boolean[2];
    private int victim;

    public void lock() {
        int i = ThreadID.get();
        int j = 1 - i;
        flag[i] = true;
        victim = i;
        while (flag[j] && victim == i) {
        }
        ; // spin
    }

    public void unlock() {
        int i = ThreadID.get();
        flag[i] = false;
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


