/*
 * SequentialHeap.java
 *
 * Created on March 10, 2007, 10:45 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

/**
 * Sequential heap.
 *
 * @param T type manged by heap
 * @author mph
 */
public class SequentialHeap<T> implements PQueue<T> {
    private static final int ROOT = 1;
    int next;
    HeapNode<T>[] heap;

    /**
     * Constructor
     *
     * @param capacity maximum number of items heap can hold
     */
    public SequentialHeap(int capacity) {
        next = 1;
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
        int child = next++;
        heap[child].init(item, priority);
        while (child > ROOT) {
            int parent = child / 2;
            int oldChild = child;
            if (heap[child].priority < heap[parent].priority) {
                swap(child, parent);
                child = parent;
            } else {
                return;
            }
        }
    }

    /**
     * Returns (does not remove) least item in heap.
     *
     * @return least item.
     */
    public T getMin() {
        return heap[ROOT].item;
    }

    /**
     * Returns and removes least item in heap.
     *
     * @return least item.
     */
    public T removeMin() {
        int bottom = --next;
        T item = heap[ROOT].item;
        swap(ROOT, bottom);
        if (bottom == ROOT) {
            return item;
        }
        int child = 0;
        int parent = ROOT;
        while (parent < heap.length / 2) {
            int left = parent * 2;
            int right = (parent * 2) + 1;
            if (left >= next) {
                break;
            } else if (right >= next || heap[left].priority < heap[right].priority) {
                child = left;
            } else {
                child = right;
            }
            // If child higher priority than parent swap then else stop
            if (heap[child].priority < heap[parent].priority) {
                swap(parent, child); // Swap item, key, and tag of heap[i] and heap[child]
                parent = child;
            } else {
                break;
            }
        }
        return item;
    }

    private void swap(int i, int j) {
        HeapNode<T> node = heap[i];
        heap[i] = heap[j];
        heap[j] = node;
    }

    public boolean isEmpty() {
        return next == 0;
    }

    public void sanityCheck() {
        int stop = next;
        for (int i = ROOT; i < stop; i++) {
            int left = i * 2;
            int right = (i * 2) + 1;
            if (left < stop && heap[left].priority < heap[i].priority) {
                System.out.println("Heap property violated:");
                System.out.printf("\theap[%d] = %d, left child heap[%d] = %d\n",
                        i, heap[i].priority, left, heap[left].priority);
            }
            if (right < stop && heap[right].priority < heap[i].priority) {
                System.out.println("Heap property violated:");
                System.out.printf("\theap[%d] = %d, right child heap[%d] = %d\n",
                        i, heap[i].priority, right, heap[right].priority);
            }
        }
    }

    private static class HeapNode<S> {
        int priority;
        S item;

        /**
         * initialize node
         *
         * @param myItem
         * @param myPriority
         */
        public void init(S myItem, int myPriority) {
            item = myItem;
            priority = myPriority;
        }
    }
}
