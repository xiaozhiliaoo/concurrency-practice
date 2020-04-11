/*
 * AtomicSWMRRegister.java
 *
 * Created on July 12, 2006, 9:18 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Atomic MRSW Register from Atomic MRSW Boolean Register
 * Adapted from S. Haldar and K. Vidyasankar, Constructing 1-writer multireader
 * multivalued atomic variables from regular variables, JACM 42(1), 1995.
 * @param T object type
 * @author Maurice Herlihy
 */
public class AtomicSWMRRegister<T> {
  volatile T[] buffer;     // regular
  volatile boolean flag;   // atomic
  public AtomicSWMRRegister(T value) {
    buffer = (T[]) new Object[2];
    buffer[0] = buffer[1] = value;
    flag = false;
  }
  
  public void write(T value) {
    flag = true;
    buffer[0] = value;
    flag = false;
    buffer[1] = value;
  }
  
  public T read() {
    T value = buffer[0];
    if (flag) {
      return value;
    } else {
      return buffer[1];
    }
  }
  
}
