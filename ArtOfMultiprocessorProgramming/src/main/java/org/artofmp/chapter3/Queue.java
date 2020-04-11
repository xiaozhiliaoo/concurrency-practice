/*
 * Queue.java
 *
 * Created on January 21, 2006, 2:41 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter3;

/**
 * Queue example
 *
 * @author Maurice Herlihy
 */
public class Queue {
    int head;       // next item to dequeue
    int size;       // number of items in queue
    Object[] items; // queue contents

    public Queue(int capacity) {
        head = 0;
        size = 0;
        items = new Object[capacity];
    }

    public synchronized void enq(Object x) {
        while (size == items.length) {
            try {
                this.wait(); // wait until not full
            } catch (InterruptedException e) {
            }
            ;
        }
        int tail = (head + size) % items.length;
        items[tail] = x;
        size = size + 1;
        this.notify();
    }

    public synchronized Object deq() {
        while (size == 0) {
            try {
                this.wait(); // wait until non-empty
            } catch (InterruptedException e) {
            }
            ;
        }
        Object x = items[head];
        size = size - 1;
        head = (head + 1) % items.length;
        this.notify();
        return x;
    }

    public class FullException extends Exception {

        /**
         * Creates a new instance of FullException
         */
        public FullException() {
            super();
        }

    }
}