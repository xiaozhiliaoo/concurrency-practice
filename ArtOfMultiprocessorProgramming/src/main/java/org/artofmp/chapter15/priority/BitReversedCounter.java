/*
 * BitReversedCounter.java
 *
 * Created on March 10, 2007, 10:54 AM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter15.priority;

/**
 * Bit-reversed counter used by fine-granied heap.
 *
 * @author mph
 */
public class BitReversedCounter {
    int counter, reverse, highBit;

    BitReversedCounter(int initialValue) {
        counter = initialValue;
        reverse = 0;
        highBit = -1;
    }

    public int reverseIncrement() {
        if (counter++ == 0) {
            reverse = highBit = 1;
            return reverse;
        }
        int bit = highBit >> 1;
        while (bit != 0) {
            reverse ^= bit;
            if ((reverse & bit) != 0) break;
            bit >>= 1;
        }
        if (bit == 0)
            reverse = highBit <<= 1;
        return reverse;
    }

    public int reverseDecrement() {
        counter--;
        int bit = highBit >> 1;
        while (bit != 0) {
            reverse ^= bit;
            if ((reverse & bit) == 0) {
                break;
            }
            bit >>= 1;
        }
        if (bit == 0) {
            reverse = counter;
            highBit >>= 1;
        }
        return reverse;
    }

    public int get() {
        return counter;
    }
}