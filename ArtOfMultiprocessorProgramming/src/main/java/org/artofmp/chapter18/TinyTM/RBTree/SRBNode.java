/*
 * SRBNode.java
 *
 * Created on April 7, 2007, 8:13 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.RBTree;

import org.artofmp.chapter18.TinyTM.Copyable;

/**
 * Sequential RBNode implementation.
 * @author Maurice Herlihy
 */
public class SRBNode implements RBNode, Copyable<SRBNode> {
  int value;
  Color color;
  RBNode parent, left, right;
  
  public int getValue() {
    return value;
  }
  public void setValue(int newValue) {
    value = newValue;
  }
  public Color getColor() {
    return color;
  }
  public void setColor(Color newColor) {
    color = newColor;
  }
  public RBNode getParent() {
    return parent;
  }
  public void setParent(RBNode newParent) {
    parent = newParent;
  }
  public RBNode getLeft() {
    return left;
  }
  public void setLeft(RBNode newLeft) {
    left = newLeft;
  }
  public void setRight(RBNode newRight) {
    right = newRight;
  }
  public RBNode getRight() {
    return right;
  }  
  public void copyTo(SRBNode target) {
    target.value = value;
    target.color = color;
    target.parent = parent;
    target.left = left;
    target.right = right;
  }
}
