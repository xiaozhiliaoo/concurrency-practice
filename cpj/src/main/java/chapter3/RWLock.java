package chapter3;

/**
 * @author lili
 * @date 2020/5/30 21:04
 * @description
 * @notes
 */

import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.Sync;

public class RWLock extends ReadWrite implements ReadWriteLock {

    class RLock implements Sync {
        public void acquire() throws InterruptedException {
            beforeRead();
        }

        public void release() {
            afterRead();
        }

        public boolean attempt(long msecs)
                throws InterruptedException {
            return beforeRead(msecs);
        }
    }

    class WLock implements Sync {
        public void acquire() throws InterruptedException {
            beforeWrite();
        }

        public void release() {
            afterWrite();
        }

        public boolean attempt(long msecs)
                throws InterruptedException {
            return beforeWrite(msecs);
        }
    }


    protected final RWLock.RLock rlock = new RWLock.RLock();
    protected final RWLock.WLock wlock = new RWLock.WLock();

    public Sync readLock() {
        return rlock;
    }

    public Sync writeLock() {
        return wlock;
    }

    public boolean beforeRead(long msecs)
            throws InterruptedException {
        return true;
        // ... time-out version of beforeRead ...
    }

    public boolean beforeWrite(long msecs)
            throws InterruptedException {
        return true;
        // ... time-out version of beforeWrite ...
    }

    protected void doRead() {
    }

    protected void doWrite() {
    }

    public static void main(String[] args) {
        RWLock rwLock = new RWLock();
        Sync readLock = rwLock.readLock();
        Sync writeLock = rwLock.writeLock();
    }

}
