/*
 * DisBarrier.java
 *
 * Created on August 3, 2005, 11:07 PM
 */

package org.artofmp.chapter17.barrier;


/**
 * Dissemination Barrier
 * @author Maurice Herlihy
 */
public class DisBarrier implements Barrier {  
  int size;
  int logSize;
  Node[][] node;
  ThreadLocal<Boolean> mySense;
  ThreadLocal<Integer> myParity;
  
  /**
   * Constructor
   * @param capacity maximum number of threads 
   */
  public DisBarrier(int capacity) {
    size = capacity;
    logSize = 0;
    while (capacity != 1) {
      this.logSize++;
      capacity >>= 1;
    }
    node = new Node[logSize][size];
    for (int r = 0; r < logSize; r++) {
      for (int i = 0; i < size; i++) {
        node[r][i] = new Node();
      }
    }
    int distance = 1;
    for (int r = 0; r < logSize; r++) {
      for (int i = 0; i < size; i++) {
        node[r][i].partner = node[r][(i + distance) % size];
      }
      distance *= 2;
    }
    this.mySense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return true; };
    };
    this.myParity = new ThreadLocal<Integer>() {
      protected Integer initialValue() { return 0; };
    };
  }
  
  public void await() {
    int parity = myParity.get();
      boolean sense = mySense.get();
      int i = ThreadID.get();
    for (int r = 0; r < logSize; r++) {
      node[r][i].partner.flag[parity] = sense;
      while (node[r][i].flag[parity] != sense) {}
    }
    if (parity == 1) {
      mySense.set(!sense);
    }
    myParity.set(1 - parity);
  }
  
  private static class Node {
    boolean[] flag = {false, false};  // signal when done
    Node partner;                     // partner node
  }
}