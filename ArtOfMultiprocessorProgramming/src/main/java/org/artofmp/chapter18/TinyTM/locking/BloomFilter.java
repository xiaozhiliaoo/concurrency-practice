/*
 * BloomFilter.java
 *
 * Created on January 13, 2007, 11:00 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import java.util.BitSet;

/**
 * Restricted Bloom Filter Implementation. Uses two hash functions.
 * Warning: This implementation works poorly when elements are Integers, because
 * an Integer's hash code is the actual integer value.
 *
 * @author Maurice Herlihy
 */
public class BloomFilter {
    BitSet filter;
    private static final int LO_MASK = 0x0000FFFF;
    private static final int HI_MASK = 0xFFFF0000;
    private static final int SHIFT = 16;

    public BloomFilter(int size) {
        filter = new BitSet(size);
    }

    private int hash0(Object x) {
        return (x.hashCode() & LO_MASK) % filter.size();
    }

    private int hash1(Object x) {
        return ((x.hashCode() & HI_MASK) >> SHIFT) % filter.size();
    }

    public void add(Object x) {
        filter.set(hash0(x));
        filter.set(hash1(x));
    }

    public boolean contains(Object x) {
        return filter.get(hash0(x)) && filter.get(hash1(x));
    }

    public void clear() {
        filter.clear();
    }
}
