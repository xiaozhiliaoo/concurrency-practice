/*
 * Bakery.java
 *
 * Created on January 21, 2006, 1:20 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 *
 */

package org.artofmp.chapter2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Lamport's Bakery lock (simplified)
 *
 * @author Maurice Herlihy
 */
public class Bakery implements Lock {
    volatile boolean[] flag;
    volatile Label[] label;

    public Bakery(int threads) {
        label = new Label[threads];
        for (int i = 0; i < label.length; i++) {
            label[i] = new Label();
        }
        flag = new boolean[threads];
    }

    public void lock() {
        int me = ThreadID.get();
        flag[me] = true;
        int max = Label.max(label);
        label[me] = new Label(max + 1);
        while (conflict(me)) {
        }
        ;  // spin
    }

    public void unlock() {
        flag[ThreadID.get()] = false;
    }

    private boolean conflict(int me) {
        for (int i = 0; i < label.length; i++) {
            if (i != me && flag[i] && label[me].compareTo(label[i]) < 0) {
                return true;
            }
        }
        return false;
    }

    // Any class implementing Lock must provide these methods
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock(long time,
                           TimeUnit unit)
            throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    public boolean tryLock() {
        throw new UnsupportedOperationException();
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new UnsupportedOperationException();
    }

    static class Label implements Comparable<Label> {
        int counter;
        int id;

        Label() {
            counter = 0;
            id = ThreadID.get();
        }

        Label(int c) {
            counter = c;
            id = ThreadID.get();
        }

        static int max(Label[] labels) {
            int c = 0;
            for (Label label : labels) {
                c = Math.max(c, label.counter);
            }
            return c;
        }

        public int compareTo(Label other) {
            if (this.counter < other.counter
                    || (this.counter == other.counter && this.id < other.id)) {
                return -1;
            } else if (this.counter > other.counter) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}
