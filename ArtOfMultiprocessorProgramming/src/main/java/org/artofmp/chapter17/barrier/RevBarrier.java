package org.artofmp.chapter17.barrier;
/*
 * RevBrrier.java
 * Exercise: is this barrier implementation correct?
 * Created on August 3, 2005, 11:07 PM
 */

import java.util.concurrent.atomic.AtomicInteger;

public class RevBarrier implements Barrier {
  int radix;
  ThreadLocal<Boolean> threadSense;
  int leaves;
  Node[] leaf;
  public RevBarrier(int _size, int _radix) {
    radix = _radix;
    leaves = 0;
    this.leaf = new Node[_size / _radix];
    int depth = 0;
    threadSense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return true; };
    };
    // compute tree depth
    while (_size > 1) {
      depth++;
      _size = _size / _radix;
      assert _size > 0;
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
    public Node(Node _parent) {
      this();
      parent = _parent;
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