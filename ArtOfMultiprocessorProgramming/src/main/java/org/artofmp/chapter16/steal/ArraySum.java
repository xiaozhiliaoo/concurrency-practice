/*
 * ArraySum.java
 *
 * Created on December 9, 2006, 5:28 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Divide-and-conquer parallel array summation
 * (Exercise soluition)
 *
 * @author Maurice Herlihy
 */
public class ArraySum {

    static ExecutorService exec = Executors.newCachedThreadPool();

    static int sum(int[] a) throws InterruptedException, ExecutionException {
        Future<Integer> future = exec.submit(new SumTask(a, 0, a.length));
        return future.get();
    }

    static class SumTask implements Callable<Integer> {
        int[] a;
        int start;
        int size;

        public SumTask(int[] a, int start, int size) {
            this.a = a;
            this.start = start;
            this.size = size;
        }

        public Integer call() throws InterruptedException, ExecutionException {
            if (size == 1) {
                return a[start];
            } else {
                int lhsSize = size / 2;
                int rhsStart = lhsSize + 1;
                int rhsSize = size - lhsSize;
                Future<Integer> lhs = exec.submit(new SumTask(a, start, lhsSize));
                Future<Integer> rhs = exec.submit(new SumTask(a, rhsStart, rhsSize));
                return rhs.get() + lhs.get();
            }
        }
    }
}
