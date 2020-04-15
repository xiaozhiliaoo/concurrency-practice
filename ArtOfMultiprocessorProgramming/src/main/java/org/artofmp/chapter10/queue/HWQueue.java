/*
 * HWQueue.java
 *
 * Created on December 27, 2005, 7:14 PM
 *
 * The Art of Multiprocessor Programming, by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package org.artofmp.chapter10.queue;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Lock-free queue baed on Maurice P. Herlihy and Jeannette M. Wing,
 * Linearizability: A correctness condition for concurrent objects.
 * ACM Transactions on Programming Languages and Systems, 12(3),
 * pages 463-- 492, July 1990. Used in an exercise.
 *
 * @author Maurice Herlihy
 */
public class HWQueue<T> {
    AtomicReference<T>[] items;
    AtomicInteger tail;
    static final int CAPACITY = 1024;

    public HWQueue() {
        items = (AtomicReference<T>[]) Array.newInstance(AtomicReference.class,
                CAPACITY);
        for (int i = 0; i < items.length; i++) {
            items[i] = new AtomicReference<T>(null);
        }
        tail = new AtomicInteger(0);
    }

    /**
     * Append item to end of queue.
     *
     * @param x item to append
     */
    public void enq(T x) {
        int i = tail.getAndIncrement();
        items[i].set(x);
    }

    /**
     * Remove and return head of queue.
     *
     * @return remove first item in queue
     */
    public T deq() {
        while (true) {
            int range = tail.get();
            for (int i = 0; i < range; i++) {
                T value = items[i].getAndSet(null);
                if (value != null) {
                    return value;
                }
            }
        }
    }
}
