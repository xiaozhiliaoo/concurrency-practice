/*
 * Defaults.java
 *
 * Created on January 7, 2007, 9:07 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;

import org.artofmp.chapter18.TinyTM.contention.BackoffManager;

/**
 * Default Classes
 * @author Maurice Herlihy
 */
public interface Defaults {
  
  /**
   * how many threads
   **/
  public static final int THREADS = 1;
  /**
   * benchmark duration in milliseconds
   **/
  public static final int TIME = 10000;
  public static final int EXPERIMENT = 100;
  public static final Class MANAGER = BackoffManager.class;
  
}
