/*
 * SeqSnapshot.java
 *
 * Created on January 19, 2006, 7:59 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Sequential snapshot. Defines the semantics for all snapshot classes.
 * @author Maurice Herlihy
 */
public class SeqSnapshot<T> implements Snapshot<T> {
  private T[] a_value;
  public SeqSnapshot(int capacity, T init) {
    a_value = (T[])new Object[capacity];
    for (int i = 0; i < a_value.length; i++) {
      a_value[i] = init;
    }
  }
  public synchronized void update(T v) {
    a_value[ThreadID.get()] = v;
  }
  public synchronized T[] scan() {
    T[] result = (T[]) new Object[a_value.length];
    for (int i = 0; i < a_value.length; i++)
      result[i] = a_value[i];
    return result;
  }
}
