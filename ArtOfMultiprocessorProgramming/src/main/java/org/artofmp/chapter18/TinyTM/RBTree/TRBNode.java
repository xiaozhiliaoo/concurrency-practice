/*
 * TRBNode.java
 *
 * Created on April 7, 2007, 9:12 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.RBTree;

import org.artofmp.chapter18.TinyTM.AtomicObject;
import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.locking.LockObject;

/**
 *
 * @author mph
 */
public class TRBNode implements RBNode {
  AtomicObject<SRBNode> atomic;
  
  public TRBNode(Class<AtomicObject<SRBNode>> _class) throws InstantiationException, IllegalAccessException {
    atomic = new LockObject<SRBNode>(new SRBNode());
  }

  public int getValue() {
    int value = atomic.openRead().getValue();
    if (!atomic.validate())
      throw new AbortedException();
    return value;
  }

  public void setValue(int value) {
    atomic.openWrite().setValue(value);
  }

  public Color getColor() {
    Color value = atomic.openRead().getColor();
    if (!atomic.validate())
      throw new AbortedException();
    return value;
  }

  public void setColor(Color value) {
    atomic.openWrite().setColor(value);
  }

  public RBNode getParent() {
    RBNode value = atomic.openRead().getParent();
    if (!atomic.validate())
      throw new AbortedException();
    return value;
  }

  public void setParent(RBNode value) {
    atomic.openWrite().setParent(value);
  }

  public RBNode getLeft() {
    RBNode value = atomic.openRead().getLeft();
    if (!atomic.validate())
      throw new AbortedException();
    return value;
  }

  public void setLeft(RBNode value) {
    atomic.openWrite().setLeft(value);
  }

  public RBNode getRight() {
    RBNode value = atomic.openRead().getRight();
    if (!atomic.validate())
      throw new AbortedException();
    return value;
  }

  public void setRight(RBNode value) {
    atomic.openWrite().setRight(value);
  }
  
}
