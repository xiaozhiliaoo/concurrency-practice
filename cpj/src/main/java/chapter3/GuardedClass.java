package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:02
 */
public class GuardedClass {                     // Generic code sketch
    protected boolean cond = false;

    // PRE: lock held
    protected void awaitCond() throws InterruptedException {
        while (!cond) wait();
    }

    public synchronized void guardedAction() {
        try {
            awaitCond();
        } catch (InterruptedException ie) {
            // fail
        }

        // actions
    }
}