/*
 * Network.java
 *
 * Created on June 9, 2006, 8:08 PM
 * 
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter12.counting;

/**
 * Balancing network interface.
 *
 * @author Maurice Herlihy
 */
public interface Network {
  public int traverse(int input);
}
