/*
 * UnboundedQueue.java
 *
 * Created on March 8, 2006, 8:02 PM
 *
 * From "The Art of Multiprocessor Programming"
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter10.queue;

import java.util.concurrent.locks.ReentrantLock;

/**
 * UnboundedQueue, blocking queue
 *
 * @param T item type
 * @author Maurice Herlihy
 */
public class UnboundedQueue<T> {

    /**
     * Lock out other enqueuers (dequeuers)
     */
    ReentrantLock enqLock, deqLock;
    /**
     * First entry in queue.
     */
    Node head;
    /**
     * Last entry in queue.
     */
    Node tail;
    /**
     * Queue size.
     */
    int size;

    /**
     * Constructor.
     */
    public UnboundedQueue() {
        head = new Node(null);
        tail = head;
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
    }

    /**
     * @return remove first item in queue
     * @throws EmptyException
     */
    public T deq() throws EmptyException {
        T result;
        deqLock.lock();
        try {
            if (head.next == null) {
                throw new EmptyException();
            }
            result = head.next.value;
            head = head.next;
        } finally {
            deqLock.unlock();
        }
        return result;
    }

    /**
     * Appende item to end of queue.
     *
     * @param x item to append
     */
    public void enq(T x) {
        if (x == null) throw new NullPointerException();
        enqLock.lock();
        try {
            Node e = new Node(x);
            tail.next = e;
            tail = e;
        } finally {
            enqLock.unlock();
        }
    }

    /**
     * Individual queue item.
     */
    protected class Node {
        /**
         * Actual value of queue item.
         */
        public T value;
        /**
         * next item in queue
         */
        public Node next;

        /**
         * Constructor
         *
         * @param x Value of item.
         */
        public Node(T x) {
            value = x;
            next = null;
        }
    }
}
