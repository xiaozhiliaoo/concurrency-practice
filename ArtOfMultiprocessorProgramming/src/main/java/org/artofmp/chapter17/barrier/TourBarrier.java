/*
 * TourBarrier.java
 *
 * Created on March 25, 2006, 6:21 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

/**
 * Tournament tree barrier.
 * @author Maurice Herlihy
 */
class TourBarrier implements Barrier {
  int leaves;
  Node[] leaf;
  ThreadLocal<Boolean> threadSense;
  public TourBarrier(int _size) {
    this.leaf = new Node[_size / 2];
    this.leaves = 0;
    int depth = 0;
    // compute tree depth
    while (_size > 1) {
      depth++;
      _size = _size / 2;
      assert _size > 0;
    }
    this.threadSense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return true; };
    };
    Node root = new Node(null);
    build(root, depth - 1);
  }
  // recursive tree constructor
  void build(Node parent, int depth) {
    // are we at a leaf node?
    if (depth == 0) {
      leaf[leaves++] = parent;
    } else {
      Node active = new Node(parent);
      Node passive = new Node();
      active.partner = passive;
      passive.partner = active;
      build(active, depth - 1);
      build(passive, depth - 1);
    }
  }
  public void await() {
    int me = ThreadID.get();
    Node myLeaf = leaf[me / 2];
    boolean sense = threadSense.get();
    myLeaf.await(sense);
    threadSense.set(!sense);
  }
  
  // tree node
  private static class Node {
    volatile boolean flag;      // signal when done
    boolean active;             // active or passive?
    Node parent;                // parent node
    Node partner;               // partner node
    // create passive node
    Node() {
      flag    = false;
      active  = false;
      partner = null;
      parent  = null;
    }
    // create active node
    Node(Node _parent) {
      this();
      parent = _parent;
      active = true;
    }
    void await(boolean sense) {
      if (this.active) { // I'm active
        if (parent != null) {
          while (this.flag != sense) {}; // wait for partner
          this.parent.await(sense);  // wait for parent
          this.partner.flag = sense; // tell partner
        }
      } else {                  // I'm passive
        this.partner.flag = sense; // tell partner
        while (this.flag != sense) {}; // wait for partner
      }
    }
  }
}