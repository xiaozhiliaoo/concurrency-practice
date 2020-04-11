/*
 * Snapshot.java
 *
 * Created on January 19, 2006, 10:39 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Interface for snapshot implementations
 * @author Maurice Herlihy
 */
public interface Snapshot<T> {
  
  public void update(T v);
  public T[] scan();
  
}
