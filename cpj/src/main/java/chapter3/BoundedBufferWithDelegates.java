package chapter3;

/**
 * @author lili
 * @date 2020/5/30 20:00
 * @description
 * @notes
 */
public final class BoundedBufferWithDelegates {
    private Object[] array;
    private Exchanger putter;
    private Exchanger taker;

    public BoundedBufferWithDelegates(int capacity)
            throws IllegalArgumentException {
        if (capacity <= 0) throw new IllegalArgumentException();
        array = new Object[capacity];
        putter = new Exchanger(capacity);
        taker = new Exchanger(0);
    }


    public void put(Object x) throws InterruptedException {
        putter.exchange(x);
    }

    public Object take() throws InterruptedException {
        return taker.exchange(null);
    }

    void removedSlotNotification(Exchanger h) { // relay
        if (h == putter) taker.addedSlotNotification();
        else putter.addedSlotNotification();
    }

    protected class Exchanger {                 // Inner class
        protected int ptr = 0;          // circular index
        protected int slots;            // number of usable slots
        protected int waiting = 0;      // number of waiting threads

        Exchanger(int n) {
            slots = n;
        }

        synchronized void addedSlotNotification() {
            ++slots;
            if (waiting > 0) // unblock a single waiting thread
                notify();
        }

        Object exchange(Object x) throws InterruptedException {
            Object old = null; // return value

            synchronized (this) {
                while (slots <= 0) { // wait for slot
                    ++waiting;
                    try {
                        wait();
                    } catch (InterruptedException ie) {
                        notify();
                        throw ie;
                    } finally {
                        --waiting;
                    }
                }

                --slots;             // use slot
                old = array[ptr];
                array[ptr] = x;
                ptr = (ptr + 1) % array.length; // advance position
            }

            removedSlotNotification(this); // notify of change
            return old;
        }
    }
}