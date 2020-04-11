/*
 * RegMRSWRegister.java
 *
 * Created on January 18, 2006, 8:31 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Regular multi-reader single-writer register. Holds only Byte values.
 * @author Maurice Herlihy
 */
public class RegMRSWRegister implements Register<Byte> {
  private static int RANGE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1;
  // regular boolean mult-reader single-writer register
  boolean[] r_bit = new boolean[RANGE]; 
  public RegMRSWRegister(int capacity) {
    r_bit[0] = true; // least value
  }
  public void write(Byte x) {
    r_bit[x] = true;
    for (int i = x - 1; i >= 0; i--)
      r_bit[i] = false;
  }
  public Byte read() {
    for (int i = 0; i < RANGE; i++)
      if (r_bit[i]) {
        return (byte)i;
      }
    return -1;  // never reached
  }
}
