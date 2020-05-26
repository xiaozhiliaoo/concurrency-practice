/*
 * SequentialNode.java
 *
 * Created on February 6, 2007, 9:24 PM
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 *
 */

package org.artofmp.chapter18.TinyTM.list;


import org.artofmp.chapter18.TinyTM.Copyable;

/**
 * @param <T> type
 * @author mph
 */
public class SNode<T> implements INode<T>, Copyable<SNode<T>> {
  int key;
  T item;
  INode<T> next;
  
  public SNode() {}
  
  public SNode(int key, T item) {
    this.item = item;
    this.key = key;
  }
  public SNode(T item) {
    this(item.hashCode(), item);
  }  
  public SNode(T item, INode<T> next) {
    this(item);
    this.next = next;
  }

  public int getKey() {
    return key;
  }

  public void setKey(int value) {
    key = value;
  }
  public T getItem() {
    return item;
  }

  public void setItem(T value) {
    item = value;
  }

  public INode<T> getNext() {
    return next;
  }

  public void setNext(INode<T> value) {
    next = value;
  }

  public void copyTo(SNode<T> target) {
    target.key  = key;
    target.item = item;
    target.next = next;
  }
  
}
