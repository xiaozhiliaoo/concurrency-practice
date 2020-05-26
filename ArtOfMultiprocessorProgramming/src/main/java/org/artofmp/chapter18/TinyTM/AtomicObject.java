/*
 * AtomicObject.java
 *
 * Created on January 25, 2007, 9:09 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM;

/**
 * Base class for transactional synchronization state.
 *
 * @param <T> type
 * @author Maurice Herlihy
 */
public abstract class AtomicObject<T extends Copyable<T>> {
    protected Class<T> myClass;
    protected T _init;

    @SuppressWarnings("unchecked")
    public AtomicObject(T init) {
        _init = init;
        myClass = (Class<T>) init.getClass();
    }

    public abstract T openRead();

    public abstract T openWrite();

    public abstract boolean validate();
}
