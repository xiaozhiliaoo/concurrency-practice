/*
 * UDEQueue.java
 *
 * Created on March 3, 2007, 11:48 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Unbounded double-ended Queue
 *
 * @author Maurice Herlihy
 */
public class UDEQueue {
    private final static int LOG_CAPACITY = 4;
    private volatile CircularArray tasks;
    volatile int bottom;
    AtomicReference<Integer> top;

    public UDEQueue(int LOG_CAPACITY) {
        tasks = new CircularArray(LOG_CAPACITY);
        top = new AtomicReference<Integer>(0);
        bottom = 0;
    }

    boolean isEmpty() {
        int localTop = top.get();
        int localBottom = bottom; // read bottom after top
        return (localBottom <= localTop);
    }

    public void pushBottom(Runnable r) {
        int oldBottom = bottom;
        int oldTop = top.get();
        CircularArray currentTasks = tasks;
        int size = oldBottom - oldTop;
        if (size >= currentTasks.capacity() - 1) {
            currentTasks = currentTasks.resize(oldBottom, oldTop);
            tasks = currentTasks;
        }
        tasks.put(oldBottom, r);
        bottom = oldBottom + 1;
    }

    public Runnable popTop() {
        int oldTop = top.get();
        int newTop = oldTop + 1;
        int oldBottom = bottom; // important that top read before bottom `\label{line:steal:UDEQ-BOTTOP}`
        CircularArray currentTasks = tasks;
        int size = oldBottom - oldTop;
        if (size <= 0) return null; // empty `\label{line:steal:UDEQ-POPEmpty}`
        Runnable r = tasks.get(oldTop);
        if (top.compareAndSet(oldTop, newTop)) // fetch and increment `\label{line:steal:UDEQ-CAS}`
            return r;
        return null;
    }

    public Runnable popBottom() {
        CircularArray currentTasks = tasks;
        bottom--;
        int oldTop = top.get();
        int newTop = oldTop + 1;
        int size = bottom - oldTop;
        if (size < 0) {
            bottom = oldTop;
            return null;
        }
        Runnable r = tasks.get(bottom);
        if (size > 0)
            return r;
        if (!top.compareAndSet(oldTop, newTop)) // fetch and increment `\label{line:steal:UDEQ-t+1}`
            return null; // queue is empty
        bottom = oldTop + 1;
        return r;
    }

    class CircularArray {
        private int logCapacity;
        private Runnable[] currentTasks;

        CircularArray(int logCapacity) {
            this.logCapacity = logCapacity;
            currentTasks = new Runnable[1 << logCapacity];
        }

        int capacity() {
            return 1 << this.logCapacity;
        }

        Runnable get(int i) {
            return this.currentTasks[i % capacity()];
        }

        void put(int i, Runnable task) {
            this.currentTasks[i % capacity()] = task;
        }

        CircularArray resize(int bottom, int top) {
            CircularArray newTasks =
                    new CircularArray(this.logCapacity + 1);
            for (int i = top; i < bottom; i++) {
                newTasks.put(i, this.get(i));
            }
            return newTasks;
        }
    }
}
