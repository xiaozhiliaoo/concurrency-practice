/*
 * Node.java
 *
 * Created on February 6, 2007, 9:06 PM
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 *
 */
package org.artofmp.chapter18.TinyTM.list;

/**
 * Node interface used for List example
 * @param <T> type
 * @author Maurice Herlihy
 */
public interface INode<T> {

  int getKey();

  void setKey(int value);

  T getItem();

  void setItem(T value);

  INode<T> getNext();

  void setNext(INode<T> value);
}
