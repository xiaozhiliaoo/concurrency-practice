package chapter3;

import EDU.oswego.cs.dl.util.concurrent.CondVar;
import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2022/6/19 10:14
 */
public class PThreadsStyleBuffer {
    private final Mutex mutex = new Mutex();
    private final CondVar notFull = new CondVar(mutex);
    private final CondVar notEmpty = new CondVar(mutex);
    private int count = 0;
    private int takePtr = 0;
    private int putPtr = 0;
    private final Object[] array;

    public PThreadsStyleBuffer(int capacity) {
        array = new Object[capacity];
    }

    public void put(Object x) throws InterruptedException {
        mutex.acquire();
        try {
            while (count == array.length)
                notFull.await();

            array[putPtr] = x;
            putPtr = (putPtr + 1) % array.length;
            ++count;
            notEmpty.signal();
        } finally {
            mutex.release();
        }
    }

    public Object take() throws InterruptedException {
        Object x = null;
        mutex.acquire();
        try {
            while (count == 0)
                notEmpty.await();

            x = array[takePtr];
            array[takePtr] = null;
            takePtr = (takePtr + 1) % array.length;
            --count;
            notFull.signal();
        } finally {
            mutex.release();
        }
        return x;
    }
}
