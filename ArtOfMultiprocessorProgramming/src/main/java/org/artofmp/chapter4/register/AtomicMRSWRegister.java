/*
 * AtomicMRSWRegister.java
 *
 * Created on January 19, 2006, 10:10 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Multi-reader single-writer atomic register constructed from single-reader
 * single-writer atomic registers.
 * @author Maurice Herlihy
 */
public class AtomicMRSWRegister<T> implements Register<T> {
  ThreadLocal<Long>  lastStamp;         // last timestamp written
  
  private StampedValue<T>[][] a_table;  // each entry is SRSW atomic
  public AtomicMRSWRegister(T x, int readers) {
    this.lastStamp = new ThreadLocal<Long>() {
      protected Long initialValue() { return 0L; };
    };
    a_table = (StampedValue<T>[][]) new StampedValue[readers][readers];
    StampedValue<T> value = new StampedValue<T>(x);
    for (int i = 0; i < readers; i++) {
      for (int j = 0; j < readers; j++) {
        a_table[i][j] = value;
      }
    }
  }
  public synchronized T read() {
    int me = ThreadID.get();
    StampedValue<T> value = a_table[me][me];
    for (int i = 0; i < a_table.length; i++) {
      value = StampedValue.max(value, a_table[i][me]);
    }
    for (int i = 0; i < a_table.length; i++) {
      a_table[me][i] = value;
    }
    return value.value;
  }
  public synchronized void write(T v) {
    int me = ThreadID.get();
    long stamp  = lastStamp.get() + 1;
    lastStamp.set(stamp); // remember for next time
    StampedValue<T> value = new StampedValue<T>(stamp, v);
    for (int i = 0; i < a_table.length; i++) {
      a_table[me][i] = value;
    }
  }
}

