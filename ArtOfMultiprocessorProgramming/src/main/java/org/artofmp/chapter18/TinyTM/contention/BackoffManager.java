/*
 * BackoffManager.java
 *
 * Created on January 7, 2007, 6:50 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.contention;

import org.artofmp.chapter18.TinyTM.Transaction;

import java.util.Random;

/**
 * Simple adaptive backoff contention manager.
 *
 * @author Maurice Herlihy
 */
public class BackoffManager extends ContentionManager {
    private static final int MIN_DELAY = 32;
    private static final int MAX_DELAY = 1024;
    Random random = new Random();
    Transaction rival = null;
    int delay = MIN_DELAY;

    public void resolve(Transaction me, Transaction other) {
        if (other != rival) {
            rival = other;
            delay = MIN_DELAY;
        }
        if (delay < MAX_DELAY) {            // be patient
            try {
                Thread.sleep(random.nextInt(delay));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            delay = 2 * delay;
        } else {                          // patience exhausted
            other.abort();
            delay = MIN_DELAY;
        }
    }
}
