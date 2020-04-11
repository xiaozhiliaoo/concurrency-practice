/*
 * RegBoolMRSWRegister.java
 *
 * Created on January 12, 2006, 10:20 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Regular Boolean multi-reader single-writer register
 * @author Maurice Herlihy
 */
public class RegBooleanMRSWRegister implements Register<Boolean> {
  ThreadLocal<Boolean> last;
  private boolean s_value;
  RegBooleanMRSWRegister(int capacity) {
    this.last = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return false; };
    };
  }
  public void write(Boolean x) {
    if (x != last.get()) {  // if new value different ...
      last.set(x);          // remember new value
      s_value =x;           // update register
    }
  }
  public Boolean read() {
    return s_value;
  }
}
