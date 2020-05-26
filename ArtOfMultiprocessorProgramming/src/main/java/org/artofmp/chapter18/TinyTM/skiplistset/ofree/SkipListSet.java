/*
 * SkipListSet.java
 *
 * Created on April 9, 2007, 7:52 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package org.artofmp.chapter18.TinyTM.skiplistset.ofree;

import org.artofmp.chapter18.TinyTM.AtomicArray;
import org.artofmp.chapter18.TinyTM.skiplistset.SkipNode;

import java.util.Iterator;

/**
 * Lazy skiplist implementation.
 *
 * @param <T> type
 * @author Yossi Lev and Maurice Herlihy
 */
public final class SkipListSet<T> implements Iterable<T> {
    static final int MAX_HEIGHT = 32;
    int randomSeed = (int) System.currentTimeMillis() | 256;
    final SkipNode<T> head;
    final SkipNode<T> tail;

    public SkipListSet() {
        head = new TSkipNode<T>(MAX_HEIGHT, Integer.MIN_VALUE, null);
        tail = new TSkipNode<T>(0, Integer.MAX_VALUE, null);
        AtomicArray<SkipNode<T>> next = head.getNext();
        for (int i = 0; i < next.length; i++) {
            next.set(i, tail);
        }
    }

    private int randomLevel() {
        int x = randomSeed;
        x ^= x << 13;
        x ^= x >>> 17;
        randomSeed = x ^= x << 5;
        if ((x & 0x8001) != 0) // test highest and lowest bits
            return 0;
        int level = 1;
        while (((x >>>= 1) & 1) != 0) ++level;
        return Math.min(level, MAX_HEIGHT - 2);
    }

    /**
     * Add item to set
     *
     * @param v to add
     * @return <code>true</code> iff set modified
     */
    public boolean add(T v) {
        int topLevel = randomLevel();
        @SuppressWarnings("unchecked")
        SkipNode<T>[] preds = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        @SuppressWarnings("unchecked")
        SkipNode<T>[] succs = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        if (find(v, preds, succs) != -1) {
            return false;
        }
        SkipNode<T> newNode = new TSkipNode<T>(topLevel + 1, v);
        for (int level = 0; level <= topLevel; level++) {
            newNode.getNext().set(level, succs[level]);
            preds[level].getNext().set(level, newNode);
        }
        return true;
    }

    public boolean contains(T v) {
        @SuppressWarnings("unchecked")
        SkipNode<T>[] preds = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        @SuppressWarnings("unchecked")
        SkipNode<T>[] succs = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        return find(v, preds, succs) != -1;
    }

    /**
     * Remove item from set.
     *
     * @param v item to remove
     * @return <code>true</code> iff set changed
     */
    public boolean remove(T v) {
        SkipNode<T> nodeToDelete = null;
        boolean isMarked = false;
//    int topLevel = -1;
        @SuppressWarnings("unchecked")
        SkipNode<T>[] preds = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        @SuppressWarnings("unchecked")
        SkipNode<T>[] succs = (SkipNode<T>[]) new SkipNode[MAX_HEIGHT];
        while (true) {
            int lFound = find(v, preds, succs);
            if (lFound != -1) {
                nodeToDelete = succs[lFound];
                AtomicArray<SkipNode<T>> next = nodeToDelete.getNext();
                for (int level = 0; level < next.length; level++) {
                    preds[level].getNext().set(level, next.get(level));
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private int find(T x, SkipNode<T>[] preds, SkipNode<T>[] succs) {
        int v = x.hashCode();
        int lFound = -1;
        SkipNode<T> pred = head;
        for (int level = MAX_HEIGHT - 1; level >= 0; level--) {
            SkipNode<T> curr = pred.getNext().get(level);
            while (v > curr.getKey()) {
                pred = curr;
                curr = pred.getNext().get(level);
            }
            if (lFound == -1 && v == curr.getKey()) {
                lFound = level;
            }
            preds[level] = pred;
            succs[level] = curr;
        }
        return lFound;
    }

    // not thread safe!
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            SkipNode<T> cursor = head;

            public boolean hasNext() {
                return cursor.getNext().get(0) != tail;
            }

            public T next() {
                cursor = cursor.getNext().get(0);
                return cursor.getItem();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}