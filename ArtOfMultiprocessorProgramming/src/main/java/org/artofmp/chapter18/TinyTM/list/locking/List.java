/*
 * List.java
 *
 * Created on May 30, 2007, 9:36 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package org.artofmp.chapter18.TinyTM.list.locking;


import org.artofmp.chapter18.TinyTM.list.INode;

import java.util.Iterator;

/**
 * @param <T> type
 * @author Maurice Herlihy
 */
public class List<T> implements Iterable<T> {
  
  protected INode<T> first;
  
  public List() {
    first = new TNode<T>(Integer.MIN_VALUE, null);
    INode<T> next = new TNode<T>(Integer.MAX_VALUE, null);
    first.setNext(next);
  } 
    
  /**
   * Add an element to the list, if it is not already there.
   * @param item object to be added
   * @return true iff list was modified
   */
  public boolean add(T item) {
    int key = item.hashCode();
    INode<T> newNode = new TNode<T>(item);
    INode<T> prevNode = this.first;
    INode<T> currNode = prevNode.getNext();
    while (currNode.getKey() < key) {
      prevNode = currNode;
      currNode = prevNode.getNext();
    }
    if (currNode.getKey() == key) {
      return false;
    } else {
      newNode.setNext(prevNode.getNext());
      prevNode.setNext(newNode);
      return true;
    }
  }
  /**
   * Tests whether a value is in the list.
   * @param item item sought
   * @return true iff presence was confirmed.
   */
  public boolean contains(T item) {
    int key = item.hashCode();
    INode prevNode = this.first;
    INode currNode = prevNode.getNext();
    while (currNode.getKey() < key) {
      prevNode = currNode;
      currNode = prevNode.getNext();
    }
    if (currNode.getKey() == key) {
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Removes an element from the list, if it is there.
   * @param v the integer value to delete from the set
   * @return true iff v was removed
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  public boolean remove(int v) throws InstantiationException, IllegalAccessException {
    INode<T> prevNode = this.first;
    INode<T> currNode = prevNode.getNext();
    while (currNode.getKey() < v) {
      prevNode = currNode;
      currNode = prevNode.getNext();
    }
    if (currNode.getKey() == v) {
      prevNode.setNext(currNode.getNext());
      return true;
    } else {
      return false;
    }
  }
  
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      INode<T> cursor;
      {
        cursor = first.getNext();
      }
      public boolean hasNext() {
        return cursor.getKey() != Integer.MAX_VALUE;
      }
      public T next() {
        INode<T> node = cursor;
        cursor = cursor.getNext();
        return node.getItem();
      }
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
