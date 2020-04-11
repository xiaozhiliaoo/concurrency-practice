/*
 * LockFreeQueue.java
 *
 * Created on January 21, 2006, 3:25 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter3;

/**
 * Two-thread lock-free queue
 *
 * @author Maurice Herlihy
 */
public class LockFreeQueue {
    int head = 0;   // next item to dequeue
    int tail = 0;   // next empty slot
    Object[] items; // queue contents

    public LockFreeQueue(int capacity) {
        head = 0;
        tail = 0;
        items = new Object[capacity];
    }

    public void enq(Object x) {
        // spin while full
        while (tail - head == items.length) {
        }
        ; // spin
        items[tail % items.length] = x;
        tail++;
    }

    public Object deq() {
        // spin while empty
        while (tail == head) {
        }
        ; // spin
        Object x = items[head % items.length];
        head++;
        return x;
    }
}
