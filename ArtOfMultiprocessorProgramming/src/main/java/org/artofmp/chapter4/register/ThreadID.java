/*
 * ThreadID.java
 *
 * Created on January 11, 2006, 10:27 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Assigns unique contiguous ids to threads.
 * @author Maurice Herlihyh
 */
public class ThreadID {
  /**
   * The next thread ID to be assigned
   **/
  private static volatile int nextID = 0;
  /**
   * My thread-local ID.
   **/
  private static ThreadLocalID threadID = new ThreadLocalID();
  public static int get() {
    return threadID.get();
  }
  public static void reset() {
    nextID = 0;
  }
  private static class ThreadLocalID extends ThreadLocal<Integer> {
    protected synchronized Integer initialValue() {
      return nextID++;
    }
  }
}

