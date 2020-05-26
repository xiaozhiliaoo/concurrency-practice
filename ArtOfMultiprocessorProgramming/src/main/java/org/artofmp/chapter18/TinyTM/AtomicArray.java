/*
 * AtomicArray.java
 *
 * Created on May 30, 2007, 8:44 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package org.artofmp.chapter18.TinyTM;


import org.artofmp.chapter18.TinyTM.contention.ContentionManager;
import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.exceptions.PanicException;

import java.lang.reflect.Array;

/**
 * Simple Atomic Array implementation.
 * Use only for small arrays!
 *
 * @param <T> type
 * @author Maurice Herlihy
 */
@atomic
public class AtomicArray<T> {
    public final int length;
    private final Class _class;
    private final T[] primary;
    private final T[] shadow;
    private Transaction writer;
    private ReadSet readers;
    long version;
    private static final String FORMAT = "Unexpected transaction state: %s";

    /**
     * Creates a new instance of AtomicArray
     *
     * @param _class   run-time class of elements
     * @param capacity array size
     */
    public AtomicArray(Class _class, int capacity) {
        this.length = capacity;
        this._class = _class;
        primary = (T[]) Array.newInstance(_class, capacity);
        shadow = (T[]) Array.newInstance(_class, capacity);
        writer = Transaction.COMMITTED;
        readers = new ReadSet();
        version = 0;
    }

    public T get(int i) {
        Transaction me = Transaction.getLocal();
        Transaction other = null;
        ContentionManager manager = ContentionManager.getLocal();
        while (true) {
            synchronized (this) {
                other = readConflict(me);
                if (other == null) {
                    return primary[i];
                }
            }
            manager.resolve(me, other);
        }
    }

    public void set(int i, T value) {
        Transaction me = Transaction.getLocal();
        Transaction other = null;
        ContentionManager manager = ContentionManager.getLocal();
        while (true) {
            synchronized (this) {
                other = writeConflict(me);
                if (other == null) {
                    primary[i] = value;
                    return;
                }
            }
            manager.resolve(me, other);
        }
    }

    /**
     * Tries to open object for reading. Returns reference to conflictin transaction, if one exists
     **/
    private Transaction readConflict(Transaction me) {
        // not in a transaction
        if (me.getStatus() == Transaction.Status.COMMITTED) {
            // restore object if latest writer aborted
            if (writer.getStatus() == Transaction.Status.ABORTED) {
                restore();
                version++;
                writer = Transaction.COMMITTED;
            }
            return null;
        }
        // Am I still active?
        if (me.getStatus() != Transaction.Status.ACTIVE) {
            throw new AbortedException();
        }
        // Have I already opened this object?
        if (writer == me) {
            return null;
        }
        switch (writer.getStatus()) {
            case ACTIVE:
                return writer;
            case COMMITTED:
                break;
            case ABORTED:
                restore();
                version++;
                break;
            default:
                throw new PanicException(FORMAT, writer.getStatus());
        }
        writer = Transaction.COMMITTED;
        readers.add(me);
        return null;
    }

    /**
     * Tries to open object for reading. Returns reference to conflicting transaction, if one exists
     * * @param me calling transaction
     *
     * @return a conflicting transaction, or <code>null</code> if none exists.
     */
    public Transaction writeConflict(Transaction me) {
        // not in a transaction
        if (me == null) {    // restore object if latest writer aborted
            if (writer.getStatus() == Transaction.Status.ABORTED) {
                restore();
                version++;
                writer = Transaction.COMMITTED;
            }
            return null;
        }
        if (me.getStatus() == Transaction.Status.ABORTED) {
            throw new AbortedException();
        }
        if (me == writer) {
            return null;
        }
        for (Transaction reader : readers) {
            if (reader.getStatus() == Transaction.Status.ACTIVE && reader != me) {
                return reader;
            }
        }
        readers.clear();
        switch (writer.getStatus()) {
            case ACTIVE:
                return writer;
            case COMMITTED:
                backup();
                version++;
                break;
            case ABORTED:
                restore();
                version++;
                break;
            default:
                throw new PanicException(FORMAT, writer.getStatus());
        }
        writer = me;
        return null;
    }

    private void restore() {
        System.arraycopy(shadow, 0, primary, 0, primary.length);
    }

    private void backup() {
        System.arraycopy(primary, 0, shadow, 0, primary.length);
    }

}
