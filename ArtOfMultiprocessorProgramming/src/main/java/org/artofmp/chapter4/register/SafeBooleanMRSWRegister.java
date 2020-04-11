/*
 * SafeBooleanMRSWRegister.java
 *
 * Created on January 11, 2006, 10:23 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * tableright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Safe Boolean multi-reader single-writer register
 * @author Maurice Herlihy
 */
public class SafeBooleanMRSWRegister implements Register<Boolean> {
  boolean[] s_table; // array of safe single-reader/single-writer registers
  
  /** Creates a new instance of SafeBooleanMRSWRegister */
  public SafeBooleanMRSWRegister(int capacity) {
    s_table = new boolean[capacity];
  }
  
  public Boolean read() {
    return s_table[ThreadID.get()];
  }
  public void write(Boolean x) {
    for (int i = 0; i < s_table.length; i++)
      s_table[i] = x;
  }
  
}
