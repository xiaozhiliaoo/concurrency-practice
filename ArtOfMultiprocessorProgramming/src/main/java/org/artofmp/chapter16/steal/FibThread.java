package org.artofmp.chapter16.steal;
/*
 * Fib.java
 *
 * Created on January 21, 2006, 5:46 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * Fibonacci implementation using threads
 *
 * @author Maurice Herlihy
 */
public class FibThread extends Thread {
    public int arg;
    public int result;

    public FibThread(int n) {
        arg = n;
        result = -1;
    }

    public void run() {
        FibThread left, right;
        if (arg < 2) {
            result = arg;
        } else {
            left = new FibThread(arg - 1);
            right = new FibThread(arg - 2);
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } catch (InterruptedException e) {
            }
            ;
            result = left.result + right.result;
        }
    }
}