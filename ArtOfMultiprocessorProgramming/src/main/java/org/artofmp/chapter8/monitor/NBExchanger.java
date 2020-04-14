/*
 * NBExchanger.java
 *
 * Created on May 21, 2007, 3:29 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2007 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Non-Blocking Exchanger.
 *
 * @author mph
 */
public class NBExchanger<T> {
    AtomicStampedReference<T> slot = new AtomicStampedReference<T>(null, 0);


    /**
     * Creates a new instance of NBExchanger
     */
    public NBExchanger() {
    }

    public T exchange(T x) throws InterruptedException {
        try {
            return doExchange(x, Integer.MAX_VALUE);
        } catch (TimeoutException cannotHappen) {
            throw new Error(cannotHappen);
        }
    }

    public T exchange(T x, long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException {
        return doExchange(x, unit.toNanos(timeout));
    }

    private T doExchange(T myItem, long nanos) throws TimeoutException {
        long timeBound = System.nanoTime() + nanos;
        int[] stampHolder = {0};
        while (true) {
            if (System.nanoTime() > timeBound)
                throw new TimeoutException();
            T yrItem = slot.get(stampHolder);
            int stamp = stampHolder[0];
            switch (stamp % 3) {
                case 0:     // slot is free
                    if (slot.compareAndSet(yrItem, myItem, stamp, stamp + 1)) {// set stamp to 1 mod 3
                        while (System.nanoTime() < timeBound) {
                            yrItem = slot.get(stampHolder);
                            if (stampHolder[0] == stamp + 2) { // stamp is 2 mod 3: there was an exchange
                                slot.set(null, stamp + 3); // increment stamp to 0 mod 3
                                return yrItem; // return other's myItem
                            }
                        } // timed out, try to reset stamp to 0 mod 3
                        if (slot.compareAndSet(myItem, null, stamp + 1, stamp)) {
                            throw new TimeoutException();
                        } else { // someone arrived at last minute
                            yrItem = slot.get(stampHolder);
                            slot.set(null, stamp + 3); // increment stamp to 0 mod 3
                            return yrItem; // return myItem of other
                        }
                    }
                    // CAS failed, retry.
                    break;
                case 1:  // someone waiting for me
                    if (slot.compareAndSet(yrItem, myItem, stamp, stamp + 1))
                        return yrItem;
                    break;
                case 2:   // others in middle of exchanging
                    break;
                default:  // impossible
                    break;
            }
        }
    }
}
