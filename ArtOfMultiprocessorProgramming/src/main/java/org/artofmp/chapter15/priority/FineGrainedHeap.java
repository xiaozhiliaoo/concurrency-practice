/*
 * FineGrainedHeap.java
 *
 * Created on March 10, 2007, 10:45 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Heap with fine-grained locking and arbitrary priorities.
 *
 * @param T type manged by heap
 * @author mph
 */
public class FineGrainedHeap<T> implements PQueue<T> {

    private static int ROOT = 1;
    private static int NO_ONE = -1;
    private Lock heapLock;
    int next;
    HeapNode<T>[] heap;

    /**
     * Constructor
     *
     * @param capacity maximum number of items heap can hold
     */
    @SuppressWarnings(value = "unchecked")
    public FineGrainedHeap(int capacity) {
        heapLock = new ReentrantLock();
        next = ROOT;
        heap = (HeapNode<T>[]) new HeapNode[capacity + 1];
        for (int i = 0; i < capacity + 1; i++) {
            heap[i] = new HeapNode<T>();
        }
    }

    /**
     * Add item to heap.
     *
     * @param item     Uninterpreted item.
     * @param priority item priority
     */
    public void add(T item, int priority) {
        heapLock.lock();
        int child = next++;
        heap[child].lock();
        heapLock.unlock();
        heap[child].init(item, priority);
        heap[child].unlock();
        while (child > ROOT) {
            int parent = child / 2;
            heap[parent].lock();
            heap[child].lock();
            int oldChild = child;
            try {
                if (heap[parent].tag == Status.AVAILABLE && heap[child].amOwner()) {
                    if (heap[child].score < heap[parent].score) {
                        swap(child, parent);
                        child = parent;
                    } else {
                        heap[child].tag = Status.AVAILABLE;
                        heap[child].owner = NO_ONE;
                        return;
                    }
                } else if (!heap[child].amOwner()) {
                    child = parent;
                }
            } finally {
                heap[oldChild].unlock();
                heap[parent].unlock();
            }
        }
        if (child == ROOT) {
            heap[ROOT].lock();
            if (heap[ROOT].amOwner()) {
                heap[ROOT].tag = Status.AVAILABLE;
                heap[child].owner = NO_ONE;
            }
            heap[ROOT].unlock();
        }
    }

    /**
     * Returns and removes lowest-priority item in heap.
     *
     * @return lowest-priority item.
     */
    public T removeMin() {
        heapLock.lock();
        int bottom = --next;
        heap[bottom].lock();
        heap[ROOT].lock();
        heapLock.unlock();
        if (heap[ROOT].tag == Status.EMPTY) {
            heap[ROOT].unlock();
            heap[bottom].lock();
            return null;
        }
        T item = heap[ROOT].item;
        heap[ROOT].tag = Status.EMPTY;
        swap(bottom, ROOT);
        heap[bottom].owner = NO_ONE;
        heap[bottom].unlock();
        if (heap[ROOT].tag == Status.EMPTY) {
            heap[ROOT].unlock();
            return item;
        }
        int child = 0;
        int parent = ROOT;
        while (parent < heap.length / 2) {
            int left = parent * 2;
            int right = (parent * 2) + 1;
            heap[left].lock();
            heap[right].lock();
            if (heap[left].tag == Status.EMPTY) {
                heap[right].unlock();
                heap[left].unlock();
                break;
            } else if (heap[right].tag == Status.EMPTY || heap[left].score < heap[right].score) {
                heap[right].unlock();
                child = left;
            } else {
                heap[left].unlock();
                child = right;
            }
            if (heap[child].score < heap[parent].score) {
                swap(parent, child);
                heap[parent].unlock();
                parent = child;
            } else {
                heap[child].unlock();
                break;
            }
        }
        heap[parent].unlock();
        return item;
    }

    private void swap(int i, int j) {
        int _owner = heap[i].owner;
        heap[i].owner = heap[j].owner;
        heap[j].owner = _owner;
        T _item = heap[i].item;
        heap[i].item = heap[j].item;
        heap[j].item = _item;
        int _priority = heap[i].score;
        heap[i].score = heap[j].score;
        heap[j].score = _priority;
        Status _tag = heap[i].tag;
        heap[i].tag = heap[j].tag;
        heap[j].tag = _tag;
    }

    public void sanityCheck() {
        int stop = next;
        for (int i = ROOT; i < stop; i++) {
            int left = i * 2;
            int right = (i * 2) + 1;
            if (left < stop && heap[left].score < heap[i].score) {
                System.out.println("Heap property violated:");
                System.out.printf("\theap[%d] = %d, left child heap[%d] = %d\n", i, heap[i].score, left, heap[left].score);
            }
            if (right < stop && heap[right].score < heap[i].score) {
                System.out.println("Heap property violated:");
                System.out.printf("\theap[%d] = %d, right child heap[%d] = %d\n", i, heap[i].score, right, heap[right].score);
            }
        }
    }

    private static enum Status {
        EMPTY, AVAILABLE, BUSY
    }

    private static class HeapNode<S> {

        Status tag;
        int score;
        S item;
        int owner;
        Lock lock;

        /**
         * initialize node
         *
         * @param myItem
         * @param myPriority
         */
        public void init(S myItem, int myPriority) {
            item = myItem;
            score = myPriority;
            tag = Status.BUSY;
            owner = ThreadID.get();
        }

        public HeapNode() {
            tag = Status.EMPTY;
            lock = new ReentrantLock();
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }

        public boolean amOwner() {
            switch (tag) {
                case EMPTY:
                    return false;
                case AVAILABLE:
                    return false;
                case BUSY:
                    return owner == ThreadID.get();
            }
            return false; // not reached
        }
    }
}