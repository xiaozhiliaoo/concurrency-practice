/*
 * RWStripedHashSet.java
 *
 * Created on November 15, 2006, 3:35 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter13.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Concurrent Cuckoo hashing using lock striping.
 * Uses R/W locks for resizing. Exercise solution.
 * @param T item type
 * @author Maurice Herlihy
 */
public class RWStripedHashSet<T> extends BaseHashSet<T>{
  final Lock readLock;
  final Lock writeLock;
  volatile Lock[] locks;
  
  /**
   * Constructor
   * @param capacity Initial number of  buckets.
   */
  public RWStripedHashSet(int capacity) {
    super(capacity);
    locks  = new Lock[capacity];
    for (int j = 0; j < locks.length; j++) {
      locks[j] = new ReentrantLock();
    }
    ReadWriteLock rwLock = new ReentrantReadWriteLock();
    readLock = rwLock.readLock();
    writeLock = rwLock.writeLock();
  }
  
  /**
   * double the set size
   */
  public void resize() {
    int oldCapacity = table.length;
    writeLock.lock();
    try {
      if (oldCapacity != table.length) {
        return; // someone beat us to it
      }
      int newCapacity  = 2 * oldCapacity;
      List<T>[] oldTable = table;
      table = (List<T>[]) new List[newCapacity];
      for (int i = 0; i < newCapacity; i++)
        table[i] = new ArrayList<T>();
      initializeFrom(oldTable);
    } finally {
      writeLock.unlock();
    }
  }
  private void initializeFrom(List<T>[] oldTable) {
    for (List<T> bucket : oldTable) {
      for (T x : bucket) {
        int myBucket = Math.abs(x.hashCode() % table.length);
        table[myBucket].add(x);
      }
    }
  }
  /**
   * Synchronize before adding, removing, or testing for item
   * @param x item involved
   */
  public final void acquire(T x) {
    readLock.lock();
    int myBucket = Math.abs(x.hashCode() % locks.length);
    locks[myBucket].lock();
  }
  /**
   * synchronize after adding, removing, or testing for item
   * @param x item involved
   */
  public void release(T x) {
    readLock.unlock();
    int myBucket = Math.abs(x.hashCode() % locks.length);
    locks[myBucket].unlock();
  }
  public boolean policy() {
    return size / table.length > 4;
  }
}
