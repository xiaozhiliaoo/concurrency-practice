/*
 * ThreadID.java
 *
 * Created on January 11, 2006, 10:27 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;

/**
 * Assigns unique contiguous ids to threads.
 *
 * @author Maurice Herlihy
 */
public class ThreadID {
    /**
     * The next thread ID to be assigned
     **/
    private static volatile int nextID = 0;
    /**
     * My thread-local ID.
     **/
    private static ThreadLocalID threadID = new ThreadLocalID();

    public static int get() {
        return threadID.get();
    }

    public static void set(int index) {
        threadID.set(index);
    }

    public static void reset() {
        nextID = 0;
    }

    private static class ThreadLocalID extends ThreadLocal<Integer> {
        @Override
        protected synchronized Integer initialValue() {
            return nextID++;
        }
    }
}

