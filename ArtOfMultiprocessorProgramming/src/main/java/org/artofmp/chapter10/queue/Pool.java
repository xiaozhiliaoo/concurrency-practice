/*
 * Pool.java
 *
 * Created on 15 July 2007, 3:50 PM PDT
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter10.queue;

/**
 * @param T item type
 * @author mph
 */
public interface Pool<T> {
    void put(T item);

    T get();
}
