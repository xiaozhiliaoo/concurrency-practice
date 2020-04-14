package org.artofmp.chapter8.monitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * Queue.java
 *
 * Created on January 8, 2006, 6:04 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * Concurrent FIFO queue usin locks & conditions
 *
 * @author Maurice Herlihy
 */

public class Queue<T> {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final T[] items;
    int tail, head, count;

    public Queue(int capacity) {
        items = (T[]) new Object[capacity];
    }

    public void enq(T x) throws InterruptedException {
        lock.lock();
        try {
            //临界区内线程获取锁了
            while (count == items.length) {
                //遇到await，把锁释放了，然后把自己挂起，别的线程可以进入，在notFull.signal再次获取锁
                notFull.await();
            }
            items[tail] = x;
            if (++tail == items.length) {
              tail = 0;
            }
            ++count;
            //非空时候通知等待的消费者
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public T deq() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            T x = items[head];
            if (++head == items.length) {
                head = 0;
            }
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }
}