/*
 * SequentialRegister.java
 *
 * Created on January 11, 2006, 10:17 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Sequential Register Implementation
 *
 * @author Maurice Herlihy
 */
public class SequentialRegister<T> implements Register<T> {
    private T value;

    public T read() {
        return value;
    }

    public void write(T v) {
        value = v;
    }
}
