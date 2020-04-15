/*
 * GTreeBarrier.java
 *
 * Created on March 26, 2006, 5:07 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */
package org.artofmp.chapter17.barrier;

/**
 * Generic tree barrier.
 * Solution to homework assignment.
 **/
public class GTreeBarrier implements Barrier {
  int radix;
  int leaves;
  Node[] leaf;
  public GTreeBarrier(int size, int radix) {
    this.radix = radix;
    leaves = 0;
    this.leaf = new Node[size / radix];
    int depth = 0;
    // compute tree depth
    while (size > 1) {
      depth++;
      size = size / radix;
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
    myLeaf.await(me);
  }
  
  private class Node {
    final Barrier barrier;
    Node parent;
    final ThreadLocal<Boolean> threadSense;
    volatile boolean done;
    // construct root node
    public Node() {
      done = false;
      this.threadSense = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() { return !Node.this.done; };
      };
      parent = null;
      barrier = new SenseBarrier(radix);
    }
    public Node(Node _parent) {
      this();
      parent = _parent;
    }
    public void await(int me) {
      boolean sense = threadSense.get();
      barrier.await();
      if (me % radix == 0) {
        if (parent != null) { // root?
          parent.await(me / radix);
        }
        done = sense;
      } else {
        while (done != sense) {};
      }
      threadSense.set(!sense);
    }
  }
}