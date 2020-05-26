/*
 * SimpleLinear.java
 *
 * Created on March 9, 2007, 9:04 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

/**
 * Simple bounded priority queue.
 *
 * @param T item type
 * @author Maurice Herlihy
 */
public class SimpleLinear<T> implements PQueue<T> {
    int range;
    Bin<T>[] pqueue;

    public SimpleLinear(int range) {
        this.range = range;
        pqueue = (Bin<T>[]) new Bin[range];
        for (int i = 0; i < pqueue.length; i++) {
            pqueue[i] = new Bin<T>();
        }
    }

    /**
     * Add item to heap.
     *
     * @param item item to add
     * @param key  item's value
     */
    public void add(T item, int key) {
        pqueue[key].put(item);
    }

    /**
     * Return and remove least item
     *
     * @return least item
     */
    public T removeMin() {
        for (int i = 0; i < range; i++) {
            T item = pqueue[i].get();
            if (item != null) {
                return item;
            }
        }
        return null;
    }

}