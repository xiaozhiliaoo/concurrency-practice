package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:12
 */
public class BoundedBufferWithSemaphores {
    protected final BufferArray buff;
    protected final Semaphore putPermits;
    protected final Semaphore takePermits;

    public BoundedBufferWithSemaphores(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        buff = new BufferArray(capacity);
        putPermits = new Semaphore(capacity);
        takePermits = new Semaphore(0);
    }

    public void put(Object x) throws InterruptedException {
        putPermits.acquire();
        buff.insert(x);
        takePermits.release();
    }

    public Object take() throws InterruptedException {
        takePermits.acquire();
        Object x = buff.extract();
        putPermits.release();
        return x;
    }

    public Object poll(long msecs) throws InterruptedException {
        if (!takePermits.attempt(msecs)) return null;
        Object x = buff.extract();
        putPermits.release();
        return x;
    }

    public boolean offer(Object x, long msecs)
            throws InterruptedException {
        if (!putPermits.attempt(msecs)) return false;
        buff.insert(x);
        takePermits.release();
        return true;
    }
}
