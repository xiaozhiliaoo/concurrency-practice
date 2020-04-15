/*
 * SimpleBarrier.java
 *
 * Created on August 3, 2005, 10:48 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This implementation is intentionally incorrect!
 *
 * @author Maurice Herlihy
 */
public class SimpleBarrier implements Barrier {
    AtomicInteger count;
    int size;

    /**
     * @param n
     */
    public SimpleBarrier(int n) {
        this.count = new AtomicInteger(n);
        this.size = n;
    }

    public void await() {
        int position = count.getAndDecrement();
        if (position == 1) {         // If I'm last ...
            count.set(size);             // reset for next use
        } else {             // otherwise spin
            while (count.get() != 0) {
            }
        }
    }
}
