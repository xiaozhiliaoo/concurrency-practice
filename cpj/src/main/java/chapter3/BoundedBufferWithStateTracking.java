package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:17
 * @description
 * @notes
 */
public class BoundedBufferWithStateTracking {
    protected final Object[] array;    // the elements
    protected int putPtr = 0;           // circular indices
    protected int takePtr = 0;
    protected int usedSlots = 0;        // the count

    public BoundedBufferWithStateTracking(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        array = new Object[capacity];
    }

    public synchronized int size() {
        return usedSlots;
    }

    public int capacity() {
        return array.length;
    }

    public synchronized void put(Object x)
            throws InterruptedException {

        while (usedSlots == array.length) // wait until not full
            wait();

        array[putPtr] = x;
        putPtr = (putPtr + 1) % array.length; // cyclically inc

        if (usedSlots++ == 0)              // signal if was empty
            notifyAll();
    }

    public synchronized Object take()
            throws InterruptedException {

        while (usedSlots == 0)           // wait until not empty
            wait();

        Object x = array[takePtr];
        array[takePtr] = null;
        takePtr = (takePtr + 1) % array.length;

        if (usedSlots-- == array.length) // signal if was full
            notifyAll();
        return x;
    }

}
