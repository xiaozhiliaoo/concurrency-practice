/*
 * Counter.java
 *
 * Created on June 9, 2006, 8:22 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter12.counting;

/**
 * Final counting network layer
 *
 * @author Maurice Herlihy
 */
public class Counter extends Balancer {
  
  int state;
  final int width;
  
  /** Creates a new instance of Counter */
  public Counter(int width) {
    super();
    this.width = width;
  }

  public synchronized int traverse(int input) {
    int count = state;
    state += width;
    return count;    
  }
  
}
