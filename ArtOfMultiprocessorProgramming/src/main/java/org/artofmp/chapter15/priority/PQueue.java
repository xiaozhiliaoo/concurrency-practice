/*
 * PQueue.java
 *
 * Created on March 8, 2007, 10:49 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

/**
 * Unbounded priority queue interface
 * @param T item type
 * @author Maurice Herlihy
 */
public interface PQueue<T> {
  
  void add(T item, int priority);
  
  T removeMin();
  
}
