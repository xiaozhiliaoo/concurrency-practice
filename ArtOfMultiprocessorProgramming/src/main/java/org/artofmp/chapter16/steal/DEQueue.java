package org.artofmp.chapter16.steal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
/*
 * DEQueue.java
 *
 * Created on March 27, 2006, 9:22 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

/**
 * Double-ended Queue
 * @author Maurice Herlihy
 */
public class DEQueue {
  AtomicStampedReference<Integer> top;  // tag & top thread index
  int bottom;                           // bottom thread index
  Runnable[] tasks;                       // array of Runnable tasks
  
  /**
   * Create a new Double-Ended Queue (DEQ)
   * @param size number of outstanding jobs
   */
  public DEQueue(int size) {
    tasks = new Runnable[size];
    top = new AtomicStampedReference<Integer>(0, 0);
    bottom = 0;
  }
  /**
   * Push a new job onto the end of the queue
   * @param r (Runnable) task to push
   */
  public void pushBottom(Runnable r){
    tasks[bottom] = r;     // store task
    bottom++;            // advance bottom
  }
  /**
   * Called by thieves to steal a thread
   * @throws steal.DEQueue.AbortException Give up if we detect a synchronization conflict
   * @return an unclaimed executable task
   */
  public Runnable popTop() {
    int[] stamp = new int[1];
    int oldTop = top.get(stamp), newTop = oldTop + 1;
    int oldStamp = stamp[0], newStamp = oldStamp + 1;
    if (bottom <= oldTop) // empty
      return null;
    Runnable r= tasks[oldTop];
    if (top.compareAndSet(oldTop, newTop, oldStamp, newStamp))
      return r;
    return null;
  }
  
  // Called by local thread to get more work
  Runnable popBottom() {
    // is the queue empty?
    if (bottom == 0)  // empty
      return null;
    bottom--;
    Runnable r = tasks[bottom];
    int[] stamp = new int[1];
    int oldTop = top.get(stamp), newTop = oldTop + 1;
    int oldStamp = stamp[0], newStamp = oldStamp + 1;
    // no conflict with thieves
    if (bottom > oldTop)
      return r;
    // possible conflict: try to pop it ourselves
    if (bottom == oldTop)
      if (top.compareAndSet(oldTop, newTop, oldStamp, newStamp))
        return r;
    // queue is empty, reset bottom and top to 0
    bottom = 0;
    top.set(0, newStamp);
    return null;
  }
  
  public void reset() {
    bottom = 0;
    top.set(0, 0);   
  }
  // called by thieves to determine whether to try to steal
  boolean isEmpty() {
    return (bottom < top.getReference());
  }
}
