package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:08
 */
public class BoundedCounterWithStateVariable {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value


    static final int BOTTOM = 0, MIDDLE = 1, TOP = 2;
    protected int state = BOTTOM;  // the state variable
    protected long count = MIN;

    protected void updateState() { // PRE: synch lock held
        int oldState = state;
        if (count == MIN) state = BOTTOM;
        else if (count == MAX) state = TOP;
        else state = MIDDLE;
        if (state != oldState && oldState != MIDDLE)
            notifyAll();              // notify on transition
    }

    public synchronized long count() {
        return count;
    }

    public synchronized void inc() throws InterruptedException {
        while (state == TOP) wait();
        ++count;
        updateState();
    }

    public synchronized void dec() throws InterruptedException {
        while (state == BOTTOM) wait();
        --count;
        updateState();
    }
}
