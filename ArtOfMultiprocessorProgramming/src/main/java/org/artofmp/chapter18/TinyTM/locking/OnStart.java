/*
 * OnCommit.java
 *
 * Created on January 14, 2007, 8:52 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

/**
 * Handler for transaction start.
 * @author Maurice Herlihy
 */
public class OnStart implements Runnable {
  public void run() {
    VersionClock.setReadStamp();
  }
  
}
