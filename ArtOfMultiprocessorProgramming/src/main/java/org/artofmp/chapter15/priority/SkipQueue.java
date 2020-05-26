/*
 * SkipQueue.java
 *
 * Created on 10 Aug 2007, 5:05PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;


/**
 *
 * @param T item type
 * @author __USER__
 */
public class SkipQueue<T> {
  PrioritySkipList<T> skiplist;
  public SkipQueue() {
    skiplist = new PrioritySkipList<T>();
  }
  /**
   * Add an item to the priority queue
   * @param item add this item
   * @param priority 
   * @return true iff queue was modified
   */
  public boolean add(T item, int priority) {
    PrioritySkipList.Node<T> node = (PrioritySkipList.Node<T>)new PrioritySkipList.Node(item, priority);
    return skiplist.add(node);
  }
  /**
   * remove and return item with least priority
   * @return item with least priority
   */
  public T removeMin() {
    PrioritySkipList.Node<T> node = skiplist.findAndMarkMin();
    if (node != null) {
      skiplist.remove(node);
      return node.item;
    } else{
      return null;
    }
  }
  
}
