/*
 * AtomicSRSWRegister.java
 *
 * Created on July 13, 2006, 9:09 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Constructs single-reader/single-writer atomic register from
 * single-reader/single-writer regular register.
 * @author Maurice Herlihy
 */
public class AtomicSRSWRegister<T> implements Register<T> {
  ThreadLocal<Long>  lastStamp;           // last timestamp written
  ThreadLocal<StampedValue<T>> lastRead;  // last value read
  StampedValue<T>   r_value;    // regular SRSW timestamp-value pair
  public AtomicSRSWRegister(T value) {
    r_value = new StampedValue<T>(value);
    this.lastStamp = new ThreadLocal<Long>() {
      protected Long initialValue() { return 0L; };
    };
    this.lastRead = new ThreadLocal<StampedValue<T>>() {
      protected StampedValue<T> initialValue() { return r_value; };
    };
  }
  public T read() {
    StampedValue<T> value = r_value;
    StampedValue<T> last  = lastRead.get();
    StampedValue<T> result = StampedValue.max(value, last);
    lastRead.set(result); // remember for next time
    return result.value;
  }
  public void write(T v) {
    long stamp  = lastStamp.get() + 1;
    lastStamp.set(stamp); // remember for next time
    r_value = new StampedValue<T>(stamp, v);
  }
}
