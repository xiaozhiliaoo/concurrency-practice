/*
 * TDBarrier.java
 *
 * Created on March 26, 2006, 10:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.artofmp.chapter17.barrier;

/**
 * Interface for termination detection barrier
 * @author Maurice Herlihy
 */
public interface TDBarrier {
  /**
   * Change active/inactive state
   * @param state true if active, false if inactive
   */
  void setActive(boolean state);
  /**
   * Check if computation has terminated.
   * @return Whether all threads have become inactive.
   */
  boolean isTerminated();
  
}
