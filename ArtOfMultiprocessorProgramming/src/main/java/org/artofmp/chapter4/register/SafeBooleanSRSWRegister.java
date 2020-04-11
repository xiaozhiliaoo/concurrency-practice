/*
 * SafeBoolSRSWRegister.java
 *
 * Created on January 11, 2006, 10:20 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Safe Boolean Single-reader single-writer register
 * This class is assumed as a primitive by the other algorithms.
 * This implementation is provided only for testing the other implementations.
 * @author Maurice Herlihy
 */
public class SafeBooleanSRSWRegister implements Register<Boolean> {
  
  private Boolean value = false;
  public synchronized Boolean read() {
    return value;
  }
  public synchronized void write(Boolean v) {
    value = v;
  }
}
