package org.artofmp.chapter16.steal;

import java.util.Random;
/*
 * WorkSharingThread.java
 *
 * Created on December 7, 2006, 9:18 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * Implemementation of Rudolph, Slivkin, and Upfal work-sharing algorithm.
 *
 * @author Maurice Herlihy
 */
public class WorkSharingThread {
    Queue[] queue;
    Random random;
    private static final int THRESHOLD = 128;

    public WorkSharingThread(Queue[] queue) {
        this.queue = queue;
        this.random = new Random();
    }

    public void run() {
        int me = ThreadID.get();
        while (true) {
            Runnable task = queue[me].deq();  // dequeue a task
            if (task != null) task.run();     // run it
            int size = queue[me].size();      // time to rebalance?
            if (random.nextInt(size + 1) == size) {
                int victim = random.nextInt(queue.length);
                // lock in ID order
                int min = (victim <= me) ? victim : me;
                int max = (victim <= me) ? me : victim;
                synchronized (queue[min]) {
                    synchronized (queue[max]) {
                        balance(queue[min], queue[max]);
                    }
                }
            }
        }
    }

    private void balance(Queue q0, Queue q1) {
        Queue qMin = (q0.size() < q1.size()) ? q0 : q1;
        Queue qMax = (q0.size() < q1.size()) ? q1 : q0;
        int diff = (qMax.size() - qMin.size()) / 2;
        if (diff > THRESHOLD)
            while (qMax.size() > qMin.size())
                qMin.enq(qMax.deq());
    }
}

// vanilla bounded queue
class Queue {
    int head;       // next item to dequeue
    int size;       // number of items in queue
    Runnable[] items; // queue contents

    public Queue(int capacity) {
        head = 0;
        size = 0;
        items = new Runnable[capacity];
    }

    public synchronized int size() {
        return size;
    }

    public synchronized void enq(Runnable x) {
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

    public synchronized Runnable deq() {
        while (size == 0) {
            try {
                this.wait(); // wait until non-empty
            } catch (InterruptedException e) {
            }
            ;
        }
        Runnable x = items[head];
        size = size - 1;
        head = (head + 1) % items.length;
        this.notify();
        return x;
    }
}