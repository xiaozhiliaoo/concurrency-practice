/*
 * FibTask.java
 *
 * Created on January 21, 2006, 5:53 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;
/**
 * Fibonacci using Executor pools
 * @author Maurice Herlihy
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
class FibTask implements Callable<Integer> {
  static ExecutorService exec = Executors.newCachedThreadPool();
  int arg;
  public FibTask(int n) {
    arg = n;
  }
  public Integer call() {
    try {
      if (arg > 2) {
        Future<Integer> left = exec.submit(new FibTask(arg-1));
        Future<Integer> right = exec.submit(new FibTask(arg-2));
        return left.get() + right.get();
      } else {
        return 1;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return 1;
    }
  }
}
