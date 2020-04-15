/*
 * LockFreeQueue.java
 *
 * Created on December 29, 2005, 2:05 PM
 *
 * The Art of Multiprocessor Programming, by Maurice Herlihy and Nir Shavit.
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 20065 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter10.queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Lock-free queue. 无锁无界队列
 * Based on Michael and Scott http://doi.acm.org/10.1145/248052.248106
 * Simple, fast, and practical non-blocking and blocking concurrent queue algorithms paper
 *
 * @param T item type
 * @author Maurice Herlihy
 */
public class LockFreeQueue<T> {
    private AtomicReference<Node> head;
    private AtomicReference<Node> tail;

    public LockFreeQueue() {
        Node sentinel = new Node(null);
        this.head = new AtomicReference<Node>(sentinel);
        this.tail = new AtomicReference<Node>(sentinel);
    }

    /**
     * Append item to end of queue.
     *
     * @param item
     */
    public void enq(T item) {
        if (item == null) throw new NullPointerException();
        Node node = new Node(item); // allocate & initialize new node
        while (true) {         // keep trying
            Node last = tail.get();    // read tail
            Node next = last.next.get(); // read next
            if (last == tail.get()) { // are they consistent?
                AtomicReference[] target = {last.next, tail};
                Object[] expect = {next, last};
                Object[] update = {node, node};
                if (multiCompareAndSet(
                        (AtomicReference<T>[]) target,
                        (T[]) expect,
                        (T[]) update)) {
                    return;
                }
            }
        }
    }

    /**
     * Remove and return head of queue.
     *
     * @return remove first item in queue
     * @throws queue.EmptyException
     */
    public T deq() throws EmptyException {
        while (true) {
            Node first = head.get();
            Node last = tail.get();
            Node next = first.next.get();
            if (first == head.get()) {// are they consistent?
                if (first == last) {    // is queue empty or tail falling behind?
                    if (next == null) {    // is queue empty?
                        throw new EmptyException();
                    }
                    // tail is behind, try to advance
                    tail.compareAndSet(last, next);
                } else {
                    T value = next.value; // read value before dequeuing
                    if (head.compareAndSet(first, next))
                        return value;
                }
            }
        }
    }

    public class Node {
        public T value;
        public AtomicReference<Node> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<Node>(null);
        }
    }

    private static <T> boolean multiCompareAndSet(
            AtomicReference<T>[] target,
            T[] expect,
            T[] update) {
        return true;
    }
}
