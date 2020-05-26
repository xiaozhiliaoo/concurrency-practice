/*
 * RBNode.java
 *
 * Created on April 7, 2007, 7:52 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.skiplistset.locking;


import org.artofmp.chapter18.TinyTM.AtomicArray;
import org.artofmp.chapter18.TinyTM.AtomicObject;
import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.locking.LockObject;
import org.artofmp.chapter18.TinyTM.skiplistset.SSkipNode;
import org.artofmp.chapter18.TinyTM.skiplistset.SkipNode;

/**
 * Transactional SkipList node
 * @param <T> type
 * @author Maurice Herlihy
 */
public class TSkipNode<T> implements SkipNode<T> {
  AtomicObject<SSkipNode<T>> atomic;
  public TSkipNode(int level) {
    atomic = new LockObject<SSkipNode<T>>(new SSkipNode<T>(level));
  }
  public TSkipNode(int level, int key, T item){
    atomic = new LockObject<SSkipNode<T>>(new SSkipNode<T>(level, key, item));
  }
  public TSkipNode(int level, T item){
    atomic = new LockObject<SSkipNode<T>>(new SSkipNode<T>(level,
        item.hashCode(), item));
  }
  /**
   ** @return array of nodes further along in the skip list.
   */
  public AtomicArray<SkipNode<T>> getNext() {
    AtomicArray<SkipNode<T>> forward = atomic.openRead().getNext();
    if (!atomic.validate())
      throw new AbortedException();
    return forward;
  }
  /**
   ** @param value new array of nodes further along in the skip list.
   */
  public void setNext(AtomicArray<SkipNode<T>> value) {
    atomic.openWrite().setNext(value);
  }
  /**
   ** @return node value.
   */
  public int getKey() {
    int key = atomic.openRead().getKey();
    if (!atomic.validate())
      throw new AbortedException();
    return key;
  }
  /**
   ** @param value new node value.
   */
  public void setKey(int value) {
    atomic.openWrite().setKey(value);
  }

  public T getItem() {
    T item = atomic.openRead().getItem();
    if (!atomic.validate())
      throw new AbortedException();
    return item;
  }

  public void setItem(T value) {
    atomic.openWrite().setItem(value);
  }
}
