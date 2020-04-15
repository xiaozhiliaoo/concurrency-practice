/*
 * TreeBarrier.java
 *
 * Created on August 3, 2005, 11:07 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Combining tree barrier.
 * @author Maurice Herlihy
 */

public class TreeBarrier implements Barrier {
  int radix;    // tree fan-in
  Node[] leaf;  // array of leaf nodes
  int leaves;   // used to build tree
  ThreadLocal<Boolean> threadSense; // thread-local sense
  public TreeBarrier(int n, int r) {
    radix = r;
    leaves = 0;
    this.leaf = new Node[n / r];
    int depth = 0;
    threadSense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return true; };
    };
    // compute tree depth
    while (n > 1) {
      depth++;
      n = n / r;
    }
    Node root = new Node();
    build(root, depth - 1);
  }
  // recursive tree constructor
  void build(Node parent, int depth) {
    // are we at a leaf node?
    if (depth == 0) {
      leaf[leaves++] = parent;
    } else {
      for (int i = 0; i < radix; i++) {
        Node child = new Node(parent);
        build(child, depth - 1);
      }
    }
  }
  /**
   * Block until all threads have reached barrier.
   */
  public void await() {
    int me = ThreadID.get();
    Node myLeaf = leaf[me / radix];
    myLeaf.await();
  }
  
  private class Node {
    AtomicInteger count;
    Node parent;
    volatile boolean sense;
    // construct root node
    public Node() {
      sense = false;
      parent = null;
      count = new AtomicInteger(radix);
    }
    public Node(Node parent) {
      this();
      this.parent = parent;
    }
    public void await() {
      boolean mySense = threadSense.get();
      int position = count.getAndDecrement();
      if (position == 1) {    // I'm last
        if (parent != null) { // root?
          parent.await();
        }
        count.set(radix);     // reset counter
        sense = mySense;
      } else {
        while (sense != mySense) {};
      }
      threadSense.set(!mySense);
    }
  }
}