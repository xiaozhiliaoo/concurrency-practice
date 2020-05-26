/*
 * VersionClock.java
 *
 * Created on January 14, 2007, 5:32 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Combines global and thread-local timestamps.
 *
 * @author Maurice Herlihy
 */
public class VersionClock {
    // global clock read and advanced by all
    static AtomicLong global = new AtomicLong();
    // thread-local cached copy of global clock
    static ThreadLocal<Long> local = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            return 0L;
        }
    };

    public static void setReadStamp() {
        local.set(global.get());
    }

    public static long getReadStamp() {
        return local.get();
    }

    public static void setWriteStamp() {
        local.set(global.incrementAndGet());
    }

    public static long getWriteStamp() {
        return local.get();
    }

}
