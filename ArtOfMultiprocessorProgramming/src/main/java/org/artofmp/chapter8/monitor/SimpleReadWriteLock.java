/*
 * SimpleReadWriteLock.java
 *
 * Created on January 9, 2006, 7:11 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Maurice Herlihy
 */
public class SimpleReadWriteLock implements ReadWriteLock {

    int readers;
    boolean writer;
    Lock lock;
    Lock readLock;
    Lock writeLock;
    Condition condition;

    public SimpleReadWriteLock() {
        writer = false;
        readers = 0;
        lock = new ReentrantLock();
        readLock = new ReadLock();
        writeLock = new WriteLock();
        condition = lock.newCondition();
    }

    public Lock readLock() {
        return readLock;
    }

    public Lock writeLock() {
        return writeLock;
    }

    class ReadLock implements Lock {
        public void lock() {
            lock.lock();
            try {
                while (writer) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                    }
                }
                readers++;
            } finally {
                lock.unlock();
            }
        }

        public void unlock() {
            lock.lock();
            try {
                readers--;
                if (readers == 0)
                    condition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void lockInterruptibly() throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        public boolean tryLock() {
            throw new UnsupportedOperationException();
        }

        public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
            throw new UnsupportedOperationException();
        }

        public Condition newCondition() {
            throw new UnsupportedOperationException();
        }
    }

    protected class WriteLock implements Lock {
        public void lock() {
            lock.lock();
            try {
                while (readers > 0) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                    }
                }
                writer = true;
            } finally {
                lock.unlock();
            }
        }

        public void unlock() {
            writer = false;
            condition.signalAll();
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

        public Condition newCondition() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }
}
