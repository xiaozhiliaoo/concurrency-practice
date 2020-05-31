package chapter3;

/**
 * @author lili
 * @date 2020/5/30 21:23
 * @description
 * @notes
 */
public class CyclicBarrier {

    protected final int parties;
    protected int count;     // parties currently being waited for
    protected int resets = 0;  // times barrier has been tripped

    CyclicBarrier(int c) {
        count = parties = c;
    }

    synchronized int barrier() throws InterruptedException {
        int index = --count;
        if (index > 0) {        // not yet tripped
            int r = resets;       // wait until next reset

            do {
                wait();
            } while (resets == r);

        } else {                 // trip
            count = parties;     // reset count for next time
            ++resets;
            notifyAll();         // cause all other parties to resume
        }

        return index;
    }
}
