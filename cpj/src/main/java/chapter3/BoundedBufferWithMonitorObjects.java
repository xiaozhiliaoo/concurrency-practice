package chapter3;

/**
 * @author lili
 * @date 2020/5/30 20:01
 * @description
 * @notes
 */
public final class BoundedBufferWithMonitorObjects {
    private final Object[] array;   // the elements

    private int putPtr = 0;         // circular indices
    private int takePtr = 0;

    private int emptySlots;         // slot counts
    private int usedSlots = 0;

    private int waitingPuts = 0;    // counts of waiting threads
    private int waitingTakes = 0;

    private final Object putMonitor = new Object();
    private final Object takeMonitor = new Object();

    public BoundedBufferWithMonitorObjects(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0)
            throw new IllegalArgumentException();

        array = new Object[capacity];
        emptySlots = capacity;
    }


    public void put(Object x) throws InterruptedException {
        synchronized (putMonitor) {
            while (emptySlots <= 0) {
                ++waitingPuts;
                try {
                    putMonitor.wait();
                } catch (InterruptedException ie) {
                    putMonitor.notify();
                    throw ie;
                } finally {
                    --waitingPuts;
                }
            }
            --emptySlots;
            array[putPtr] = x;
            putPtr = (putPtr + 1) % array.length;
        }
        synchronized (takeMonitor) { // directly notify
            ++usedSlots;
            if (waitingTakes > 0)
                takeMonitor.notify();
        }
    }

    public Object take() throws InterruptedException {
        Object old = null;
        synchronized (takeMonitor) {
            while (usedSlots <= 0) {
                ++waitingTakes;
                try {
                    takeMonitor.wait();
                } catch (InterruptedException ie) {
                    takeMonitor.notify();
                    throw ie;
                } finally {
                    --waitingTakes;
                }
            }
            --usedSlots;
            old = array[takePtr];
            array[takePtr] = null;
            takePtr = (takePtr + 1) % array.length;
        }
        synchronized (putMonitor) {
            ++emptySlots;
            if (waitingPuts > 0)
                putMonitor.notify();
        }
        return old;
    }

}