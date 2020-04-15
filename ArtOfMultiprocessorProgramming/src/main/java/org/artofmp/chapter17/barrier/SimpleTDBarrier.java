/*
 * SimpleTDBarrier.java
 *
 * Created on March 26, 2006, 10:30 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simmple termination-detection barrier
 * @author Maurice Herlihy
 */
public class SimpleTDBarrier implements TDBarrier {
  AtomicInteger count;
  /**
   * 
   * @param n 
   */
  public SimpleTDBarrier(int n){
    this.count = new AtomicInteger(n);
  }
  /**
   * 
   * @param active 
   */
  public void setActive(boolean active) {
    if (active) {
      count.getAndDecrement();
    } else {
      count.getAndIncrement();
    }
  }
  
  /**
   * 
   * @return 
   */
  public boolean isTerminated() {
    return count.get() == 0;
  }
}
