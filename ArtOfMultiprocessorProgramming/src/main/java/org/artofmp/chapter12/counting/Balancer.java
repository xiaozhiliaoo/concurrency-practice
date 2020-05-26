/*
 * Balancer.java
 *
 * Created on June 9, 2006, 8:11 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter12.counting;

/**
 * A Balancer
 *
 * @author Maurice Herlihy
 */
public class Balancer implements Network {
  Boolean toggle;
  
  public Balancer() {
    toggle = true;
  }
  
  public synchronized int traverse(int input) {
    try {
      if (toggle) {
        return 0;
      } else {
        return 1;
      }
    } finally {
      toggle = !toggle;
    }
  }
  
}
