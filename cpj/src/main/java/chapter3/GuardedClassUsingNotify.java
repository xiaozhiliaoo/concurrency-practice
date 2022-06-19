package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:04
 */
public class GuardedClassUsingNotify {
    protected boolean cond = false;
    protected int nWaiting = 0; // count waiting threads

    protected synchronized void awaitCond()
            throws InterruptedException {
        while (!cond) {
            ++nWaiting; // record fact that a thread is waiting
            try {
                wait();
            } catch (InterruptedException ie) {
                notify();
                throw ie;
            } finally {
                --nWaiting; // no longer waiting
            }
        }
    }

    protected synchronized void signalCond() {
        if (cond) {                 // simulate notifyAll
            for (int i = nWaiting; i > 0; --i) {
                notify();
            }
        }
    }
}
