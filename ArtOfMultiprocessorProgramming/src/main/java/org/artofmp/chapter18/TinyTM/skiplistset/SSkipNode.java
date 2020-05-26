/*
 * SSkipNode.java
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
import org.artofmp.chapter18.TinyTM.Copyable;

/**
 * SequentialSSkipNode node
 * @param <T> type
 * @author Maurice Herlihy
 */
public class SSkipNode<T> implements SkipNode<T>, Copyable<SSkipNode<T>> {
  AtomicArray<SkipNode<T>> next;
  int key;
  T item;
  
  public SSkipNode() {}
  
  public SSkipNode(int level) {
    next = new AtomicArray<SkipNode<T>>(SkipNode.class, level);
  }
  public SSkipNode(int level, int key, T item) {
    this(level);
    this.key  = key;
    this.item = item;
  }
  /**
   ** @return array of nodes further along in the skip list.
   */
  public AtomicArray<SkipNode<T>> getNext() {
    return next;
  }
  /**
   ** @param value new array of nodes further along in the skip list.
   */
  public void setNext(AtomicArray<SkipNode<T>> value) {
    next = value;
  }
  /**
   ** @return node key.
   */
  public int getKey() {
    return key;
  }
  /**
   ** @param value new node key.
   */
  public void setKey(int value) {
    key = value;
  }
  /**
   ** @return node item.
   */
  public T getItem() {
    return item;
  }
  /**
   ** @param value new node value.
   */
  public void setItem(T value) {
    item = value;
  }
  
  public void copyTo(SSkipNode<T> target) {
    target.next = next;
    target.key     = key;
    target.item    = item;
  }
}
