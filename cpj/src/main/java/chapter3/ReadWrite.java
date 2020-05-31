package chapter3;

/**
 * @author lili
 * @date 2020/5/30 21:05
 * @description
 * @notes
 */

public abstract class ReadWrite {

    protected int activeReaders = 0;  // threads executing read
    protected int activeWriters = 0;  // always zero or one

    protected int waitingReaders = 0; // threads not yet in read
    protected int waitingWriters = 0; // same for write

    protected abstract void doRead(); // implement in subclasses

    protected abstract void doWrite();

    public void read() throws InterruptedException {
        beforeRead();
        try {
            doRead();
        } finally {
            afterRead();
        }
    }

    public void write() throws InterruptedException {
        beforeWrite();
        try {
            doWrite();
        } finally {
            afterWrite();
        }
    }

    protected boolean allowReader() {
        return waitingWriters == 0 && activeWriters == 0;
    }

    protected boolean allowWriter() {
        return activeReaders == 0 && activeWriters == 0;
    }

    protected synchronized void beforeRead()
            throws InterruptedException {
        ++waitingReaders;
        while (!allowReader()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                --waitingReaders; // roll back state
                throw ie;
            }
        }
        --waitingReaders;
        ++activeReaders;
    }

    protected synchronized void afterRead() {
        --activeReaders;
        notifyAll();
    }

    protected synchronized void beforeWrite()
            throws InterruptedException {
        ++waitingWriters;
        while (!allowWriter()) {
            try {
                wait();
            } catch (InterruptedException ie) {
                --waitingWriters;
                throw ie;
            }
        }
        --waitingWriters;
        ++activeWriters;
    }

    protected synchronized void afterWrite() {
        --activeWriters;
        notifyAll();
    }
}
