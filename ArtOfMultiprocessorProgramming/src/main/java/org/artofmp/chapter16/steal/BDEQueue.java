/*
 * BDEQueue.java
 *
 * Created on March 3, 2007, 6:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.artofmp.chapter16.steal;

import java.util.concurrent.atomic.AtomicStampedReference;

public class BDEQueue {
  Runnable[] tasks;
  volatile int bottom;
  AtomicStampedReference<Integer> top;
  public BDEQueue(int capacity) {
    tasks = new Runnable[capacity];
    top = new AtomicStampedReference<Integer>(0, 0);
    bottom = 0;
  }
  public void pushBottom(Runnable r){
    tasks[bottom] = r;     // store task
    bottom++;            // advance bottom
  }
  // called by thieves to determine whether to try to steal
  boolean isEmpty() {
    return (top.getReference() < bottom);
  }
  public Runnable popTop() {
    int[] stamp = new int[1];
    int oldTop = top.get(stamp), newTop = oldTop + 1;
    int oldStamp = stamp[0], newStamp = oldStamp + 1;
    if (bottom <= oldTop) // empty `\label{line:steal:top-empty}`
      return null;
    Runnable r= tasks[oldTop];
    if (top.compareAndSet(oldTop, newTop, oldStamp, newStamp))
      return r;
    return null;
  }
  Runnable popBottom() {
    // is the queue empty?
    if (bottom == 0)  // empty  `\label{line:steal:empty}`
      return null;
    bottom--;
    // bottom is volatile to assure all reads beyond this line see it
    Runnable r = tasks[bottom];
    int[] stamp = new int[1];
    int oldTop = top.get(stamp), newTop = 0;
    int oldStamp = stamp[0], newStamp = oldStamp + 1;
    // no conflict with thieves `\label{line:steal:noconflict}`
    if (bottom > oldTop)
      return r;
    // possible conflict: try to pop it ourselves
    if (bottom == oldTop) {
      // even if stolen by other, queue will be empty, reset bottom
      bottom = 0;
      if (top.compareAndSet(oldTop, newTop, oldStamp, newStamp))
        return r;
      } 
    return null;
  }
  public void reset() {
    bottom = 0;
    top.set(0, 0);   
  }
}
