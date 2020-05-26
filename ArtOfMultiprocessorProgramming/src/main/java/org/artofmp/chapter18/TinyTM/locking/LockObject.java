/*
 * AtomicObject.java
 *
 * Created on January 23, 2007, 11:53 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package org.artofmp.chapter18.TinyTM.locking;

import org.artofmp.chapter18.TinyTM.AtomicObject;
import org.artofmp.chapter18.TinyTM.Copyable;
import org.artofmp.chapter18.TinyTM.Transaction;
import org.artofmp.chapter18.TinyTM.exceptions.AbortedException;
import org.artofmp.chapter18.TinyTM.exceptions.PanicException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Encapsulates synchronization for locking STM.
 *
 * @param <T> type
 * @author Maurice Herlihy
 */
public class LockObject<T extends Copyable<T>> extends AtomicObject<T> {

    static final long TIMEOUT = 1000;
    ReentrantLock lock;
    volatile long stamp;
    T version;

    public LockObject(T init) {
        super(init);
        version = init;
        lock = new ReentrantLock();
        stamp = 0;
    }

    public T openRead() {
        ReadSet readSet = ReadSet.getLocal();
        switch (Transaction.getLocal().getStatus()) {
            case COMMITTED:
                return version;
            case ACTIVE:
                WriteSet writeSet = WriteSet.getLocal();
                if (writeSet.get(this) == null) {
                    if (lock.isLocked()) {
                        throw new AbortedException();
                    }
                    readSet.add(this);
                    return version;
                } else {
                    @SuppressWarnings(value = "unchecked")
                    T scratch = (T) writeSet.get(this);
                    return scratch;
                }
            case ABORTED:
                throw new AbortedException();
            default:
                throw new PanicException("unexpected transaction state");
        }
    }

    public T openWrite() {
        switch (Transaction.getLocal().getStatus()) {
            case COMMITTED:
                return version;
            case ACTIVE:
                WriteSet writeSet = WriteSet.getLocal();
                @SuppressWarnings(value = "unchecked") T scratch = (T) writeSet.get(this);
                if (scratch == null) {
                    try {
                        if (lock.isLocked()) {
                            throw new AbortedException();
                        }
                        scratch = myClass.newInstance();
                    } catch (InstantiationException ex) {
                        throw new PanicException(ex);
                    } catch (IllegalAccessException ex) {
                        throw new PanicException(ex);
                    }
                    version.copyTo(scratch);
                    writeSet.put(this, scratch);
                }
                return scratch;
            case ABORTED:
                throw new AbortedException();
            default:
                throw new PanicException("unexpected transaction state");
        }
    }

    public boolean tryLock(long timeout, TimeUnit timeUnit) {
        try {
            return lock.tryLock(timeout, timeUnit);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // restore interrupted state
            return false;
        }
    }

    public void unlock() {
        lock.unlock();
    }

    public boolean validate() {
        Transaction.Status status = Transaction.getLocal().getStatus();
        switch (status) {
            case COMMITTED:
                return true;
            case ACTIVE:
                boolean free = !lock.isLocked() || lock.isHeldByCurrentThread();
                boolean pure = stamp <= VersionClock.getReadStamp();
                return free & pure;
            case ABORTED:
                return false;
            default:
                throw new PanicException("Unexpected transaction state:" + status);
        }
    }
}