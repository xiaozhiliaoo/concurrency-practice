/*
 * SimpleSnapshot.java
 *
 * Created on January 19, 2006, 10:43 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;
import java.util.Arrays;
/**
 *
 * @author Maurice Herlihy
 */

public class SimpleSnapshot<T> implements Snapshot<T> {
  private StampedValue<T>[] a_table;  // array of atomic MRSW registers
  public SimpleSnapshot(int capacity, T init) {
    a_table = (StampedValue<T>[]) new StampedValue[capacity];
    for (int i = 0; i < capacity; i++) {
      a_table[i] = new StampedValue<T>(init);
    }
  }
  public void update(T value) {
    int me = ThreadID.get();
    StampedValue<T> oldValue = a_table[me];
    StampedValue<T> newValue =
        new StampedValue<T>((oldValue.stamp)+1, value);
    a_table[me] = newValue;
  }
  private StampedValue<T>[] collect() {
    StampedValue<T>[] copy = (StampedValue<T>[]) new StampedValue[a_table.length];
    for (int j = 0; j < a_table.length; j++)
      copy[j] = a_table[j];
    return copy;
  }
  public T[] scan() {
    StampedValue<T>[] oldCopy, newCopy;
    oldCopy = collect();
    collect: while (true) {
      newCopy = collect();
      if (! Arrays.equals(oldCopy, newCopy)) {
        oldCopy = newCopy;
        continue collect;
      }
      // clean collect
      T[] result = (T[]) new Object[a_table.length];
      for (int j = 0; j < a_table.length; j++)
        result[j] = newCopy[j].value;
      return result;
    }
  }
}
