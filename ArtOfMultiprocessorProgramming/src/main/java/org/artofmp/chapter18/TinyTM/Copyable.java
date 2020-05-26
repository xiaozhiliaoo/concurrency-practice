/*
 * Copyable.java
 *
 * Created on January 23, 2007, 11:54 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;

/**
 * Interface that exports public copyTo method
 * @param <T> type
 * @author Maurice Herlihy
 */
public interface Copyable<T> {
  void copyTo(T target);  
}

