/*
 * SkipNode.java
 *
 * Created on April 7, 2007, 7:52 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.skiplistset;


import org.artofmp.chapter18.TinyTM.AtomicArray;

/**
 * Common interface for SkipList nodes.
 * @param <T> type
 * @author Maurice Herlihy
 */
public interface SkipNode<T> {
  /**
   ** @return array of nodes further along in the skip list.
   */
  public AtomicArray<SkipNode<T>> getNext();
  /**
   * @param value new atomic array
   */
  public void setNext(AtomicArray<SkipNode<T>> value);
  
  /**
   ** @return node value.
   */
  public int getKey();
  /**
   ** @param value new node value.
   */
  public void setKey(int value);
  /**
   ** @return node value.
   */
  public T getItem();
  /**
   ** @param value new node value.
   */
  public void setItem(T value);
}
