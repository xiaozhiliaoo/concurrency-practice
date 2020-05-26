/*
 * SimpleTree.java
 *
 * Created on March 9, 2007, 9:48 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple tree-based bounded priority queue
 *
 * @param T item type
 * @author Maurice Herlihy
 */
public class SimpleTree<T> implements PQueue<T> {
    int range;

    List<TreeNode> leaves; // array of tree leaves
    TreeNode root;     // root of tree

    public SimpleTree(int logRange) {
        range = (1 << logRange);
        leaves = new ArrayList<TreeNode>(range);
        root = buildTree(logRange, 0);
    }

    TreeNode buildTree(int height, int slot) {
        TreeNode root = new TreeNode();
        root.counter = new AtomicInteger(0);
        if (height == 0) { // leaf node?
            root.bin = new Bin<T>();
            leaves.add(slot, root);
        } else {
            root.left = buildTree(height - 1, 2 * slot);
            root.right = buildTree(height - 1, (2 * slot) + 1);
            root.left.parent = root.right.parent = root;
        }
        return root;
    }

    /**
     * add item to priority queue
     *
     * @param item     new item
     * @param priority item''s priority
     */
    public void add(T item, int priority) {
        TreeNode node = leaves.get(priority);
        node.bin.put(item);
        while (node != root) {
            TreeNode parent = node.parent;
            if (node == parent.left) { // increment if ascending from left
                parent.counter.getAndIncrement();
            }
            node = parent;
        }
    }

    public T removeMin() {
        TreeNode node = root;
        while (!node.isLeaf()) {
            if (node.counter.getAndDecrement() > 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node.bin.get(); // if null pqueue is empty
    }

    public class TreeNode {
        AtomicInteger counter;    // bounded counter
        TreeNode parent;    // reference to parent
        TreeNode right;     // right child
        TreeNode left;      // left child
        Bin<T> bin;         // non-null for leaf

        public boolean isLeaf() {
            return right == null;
        }
    }
}
