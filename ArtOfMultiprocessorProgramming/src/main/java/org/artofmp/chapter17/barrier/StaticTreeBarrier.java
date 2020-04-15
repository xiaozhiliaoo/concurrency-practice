/*
 * StaticTree.java
 *
 * Created on March 25, 2006, 6:21 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Static Tree barrier.
 * For simplicity, number of threads must have form (radix^k-1)/(radix - 1)
 * @author Maurice Herlihy
 */
public class StaticTreeBarrier implements Barrier {
  int radix;      // tree fan-in
  boolean sense;  // global sense
  Node[] node;    // array of nodes
  ThreadLocal<Boolean> threadSense; // thread-local sense
  int nodes;      // used to build tree
  /**
   * Constructor
   * @param size Number of threads. (For simplicity, must be (radix^k-1)/(radix-1).
   * @param radix tree fan-in
   */
  public StaticTreeBarrier(int size, int radix) {
    this.radix = radix;
    nodes = 0;
    this.node = new Node[size];
    int depth = 0;
    // compute tree depth
    while (size > 1) {
      depth++;
      size = size / radix;
    }
    build(null, depth);
    sense = false;
    threadSense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return !sense; };
    };
  }
  // recursive tree constructor
  void build(Node parent, int depth) {
    // are we at a leaf node?
    if (depth == 0) {
      node[nodes++] = new Node(parent, 0);
    } else {
      Node myNode = new Node(parent, radix);
      node[nodes++] = myNode;
      for (int i = 0; i < radix; i++) {
        build(myNode, depth - 1);
      }
    }
  }
  
  /**
   * Spin until all threads have reached the barrier.
   */
  public void await() {
    node[ThreadID.get()].await();
  }
  
  class Node {
    final int children; // number of children
    final Node parent;
    AtomicInteger childCount; // number of children incomplete
    
    public Node(Node parent, int count) {
      this.children = count;
      this.childCount = new AtomicInteger(count);
      this.parent = parent;
    }
    
    public void await() {
      boolean mySense = threadSense.get();
      while (childCount.get() > 0) {};  // spin until children done
      childCount.set(children);         // prepare for next round
      if (parent != null) { // not root?
        parent.childDone();           // indicate child subtree completion
        while (sense != mySense) {}; // wait for global sense to change
      } else {
        sense = !sense;   // am root: toggle global sense
      }
      threadSense.set(!mySense); // toggle sense
    }
    
    public void childDone() {
      childCount.getAndDecrement();
    }
  }
}