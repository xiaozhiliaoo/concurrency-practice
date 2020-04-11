/*
 * Register.java
 *
 * Created on January 11, 2006, 10:04 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * All registers implement this interface.
 *
 * @author Maurice Herlihy
 */
public interface Register<T> {
    T read();

    void write(T v);
}
