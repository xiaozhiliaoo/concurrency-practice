/*
 * AtomicMRMWRegister.java
 *
 * Created on January 19, 2006, 10:02 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter4.register;

/**
 * Atomic MRMW register construction from Atomic MRSW register
 *
 * @author Maurice Herlihy
 */
public class AtomicMRMWRegister<T> implements Register<T> {
    private StampedValue<T>[] a_table;  // array of multi-reader single-writer registers

    public AtomicMRMWRegister(int capacity, T init) {
        a_table = (StampedValue<T>[]) new StampedValue[capacity];
        StampedValue<T> value = new StampedValue<T>(init);
        for (int j = 0; j < a_table.length; j++) {
            a_table[j] = value;
        }
    }

    public void write(T value) {
        int me = ThreadID.get();
        StampedValue<T> max = StampedValue.MIN_VALUE;
        for (int i = 0; i < a_table.length; i++) {
            max = StampedValue.max(max, a_table[i]);
        }
        a_table[me] = new StampedValue<T>(max.stamp + 1, value);
    }

    public T read() {
        StampedValue<T> max = StampedValue.MIN_VALUE;
        for (int i = 0; i < a_table.length; i++) {
            max = StampedValue.max(max, a_table[i]);
        }
        return max.value;
    }
}