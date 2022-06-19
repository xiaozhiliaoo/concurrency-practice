package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:06
 */
public class SpinLock {                   // Avoid needing to use this

    private volatile boolean busy = false;

    synchronized void release() {
        busy = false;
    }

    void acquire() throws InterruptedException {
        int itersBeforeYield = 100;    // 100 is arbitrary
        int itersBeforeSleep = 200;    // 200 is arbitrary
        long sleepTime = 1;            // 1msec is arbitrary
        int iters = 0;
        for (; ; ) {
            if (!busy) {                 // test-and-test-and-set
                synchronized (this) {
                    if (!busy) {
                        busy = true;
                        return;
                    }
                }
            }

            if (iters < itersBeforeYield) {       // spin phase
                ++iters;
            } else if (iters < itersBeforeSleep) {  // yield phase
                ++iters;
                Thread.yield();
            } else {                                // back-off phase
                Thread.sleep(sleepTime);
                sleepTime = 3 * sleepTime / 2 + 1; // 50% is arbitrary
            }
        }
    }
}
