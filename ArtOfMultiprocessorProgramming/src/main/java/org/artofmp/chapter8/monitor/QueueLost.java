/*
 * QueueLost.java
 *
 * Created on January 8, 2006, 6:38 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

/**
 * Bounded Qeeue with lost wakeup. This code is <b>intentionally incorrect</b>.
 *
 * @author Maurice Herlihy
 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class QueueLost<T> {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final T[] items;
    int tail, head, count;

    public QueueLost(int capacity) {
        items = (T[]) new Object[capacity];
    }

    public void enq(T x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[tail] = x;
            if (++tail == items.length)
                tail = 0;
            ++count;
            if (count == 1) {   // Wrong!
                notEmpty.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public T deq() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            T x = items[head];
            if (++head == items.length)
                head = 0;
            --count;
            if (count == items.length - 1) {  // Wrong
                notFull.signal();
            }
            return x;
        } finally {
            lock.unlock();
        }
    }
}
