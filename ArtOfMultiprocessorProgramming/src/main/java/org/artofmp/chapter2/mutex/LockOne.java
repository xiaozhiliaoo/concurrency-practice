package org.artofmp.chapter2.mutex;
/*
 * LockOne.java
 *
 * Created on January 21, 2006, 9:26 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * First attempt at a mutual exclusion lock.
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.lang.ThreadLocal;
import java.util.concurrent.TimeUnit;

public class LockOne implements Lock {
    private boolean[] flag = new boolean[2];
    // thread-local index, 0 or 1
    private static ThreadLocal<Integer> myIndex;

    public void lock() {
        int i = ThreadID.get();
        int j = i - 1;
        flag[i] = true;
        while (flag[j]) {
        }          // wait
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
