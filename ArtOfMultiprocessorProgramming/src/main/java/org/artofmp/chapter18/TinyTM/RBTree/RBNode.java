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

package org.artofmp.chapter18.TinyTM.RBTree;

/**
 * Red-Black Tree node
 *
 * @author Maurice Herlihy
 */
public interface RBNode {
    int getValue();        // node value

    void setValue(int newValue);

    Color getColor();    // color: red or black?

    void setColor(Color newColor);

    RBNode getParent();    // parent node

    void setParent(RBNode newParent);

    RBNode getLeft();    // left child

    void setLeft(RBNode newLeft);

    RBNode getRight();    // right child

    void setRight(RBNode newRight);
}
