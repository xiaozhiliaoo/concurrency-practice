/*
 * WriteSet.java
 *
 * Created on January 13, 2007, 11:18 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.locking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * A thread-local write set for the atomic locking BaseObject implementation.
 *
 * @author Maurice Herlihy
 */
public class WriteSet implements Iterable<Map.Entry<LockObject<?>, Object>> {
    static ThreadLocal<Map<LockObject<?>, Object>> local
            = new ThreadLocal<Map<LockObject<?>, Object>>() {
        @Override
        protected Map<LockObject<?>, Object> initialValue() {
            return new HashMap<LockObject<?>, Object>();
        }
    };

    Map<LockObject<?>, Object> map;

    public static WriteSet getLocal() {
        return new WriteSet();
    }

    private WriteSet() {
        map = local.get();
    }

    public Iterator<Map.Entry<LockObject<?>, Object>> iterator() {
        return map.entrySet().iterator();
    }

    public void unlock() {
        for (LockObject<?> x : map.keySet()) {
            x.unlock();
        }
    }

    public boolean tryLock(long timeout, TimeUnit timeUnit) {
        Stack<LockObject<?>> stack = new Stack<LockObject<?>>();
        for (LockObject<?> x : map.keySet()) {
            if (!x.tryLock(timeout, timeUnit)) {
                for (LockObject<?> y : stack) {
                    y.unlock();
                }
                return false;
            }
        }
        return true;
    }

    public Object get(LockObject<?> key) {
        return map.get(key);
    }

    public void put(LockObject<?> key, Object value) {
        map.put(key, value);
    }

    public void clear() {
        map.clear();
    }
}
