/*
 * WFSnapshot.java
 *
 * Created on January 19, 2006, 9:20 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Wait-free snapshot.
 * @author Maurice Herlihy
 */
public class WFSnapshot<T> implements Snapshot<T> {
  private StampedSnap<T>[] a_table; // array of MRSW atomic registers
  public WFSnapshot(int capacity, T init) {
    a_table = (StampedSnap<T>[]) new StampedSnap[capacity];
    for (int i = 0; i < a_table.length; i++) {
      a_table[i] = new StampedSnap<T>(init);
    }
  }
  public void update(T value) {
    int me = ThreadID.get();
    T[] snap = this.scan();
    StampedSnap<T> oldValue = a_table[me];
    StampedSnap<T> newValue =
      new StampedSnap<T>(oldValue.stamp+1, value, snap);
    a_table[me] = newValue;
  }
  private StampedSnap<T>[] collect() {
    StampedSnap<T>[] copy = (StampedSnap<T>[]) new StampedSnap[a_table.length];
    for (int j = 0; j < a_table.length; j++)
      copy[j] = a_table[j];
    return copy;
  }
  public T[] scan() {
    StampedSnap<T>[] oldCopy;
    StampedSnap<T>[] newCopy;
    boolean[] moved = new boolean[a_table.length];
    oldCopy = collect();
    collect: while (true) {
      newCopy = collect();
      for (int j = 0; j < a_table.length; j++) {
        // did any thread move?
        if (oldCopy[j].stamp != newCopy[j].stamp) {
          if (moved[j]) {       // second move
            return oldCopy[j].snap;
          } else {
            moved[j] = true;
            oldCopy = newCopy;
            continue collect;
          }
        }
      }
      // clean collect
      T[] result = (T[]) new Object[a_table.length];
      for (int j = 0; j < a_table.length; j++)
        result[j] = newCopy[j].value;
      return result;
    }
  }
}
