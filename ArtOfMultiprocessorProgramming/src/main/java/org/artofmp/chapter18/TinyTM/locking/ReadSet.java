/*
 * ReadSet.java
 *
 * Created on January 13, 2007, 11:39 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A thread-local read set for the atomic locking object implementation.
 * @author Maurice Herlihy
 */
public class ReadSet implements Iterable <LockObject<?>> {
  static ThreadLocal<Set<LockObject<?>>> local = new ThreadLocal<Set<LockObject<?>>>() {
    protected Set<LockObject<?>> initialValue() {
      return new HashSet<LockObject<?>>();
    }
  };
  
  Set<LockObject<?>> set;
  
  private ReadSet() {
    set = local.get();
  }
  public static ReadSet getLocal() {
    return new ReadSet();
  }
  
  public Iterator<LockObject<?>> iterator() {
    return set.iterator();
  }
  public void add(LockObject<?> x) {
    set.add(x);
  }
  public void clear() {
    set.clear();
  }
}
