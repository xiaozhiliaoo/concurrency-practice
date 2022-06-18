package chapter2;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2022/6/19 2:02
 */
public class CellUsingReorderedBackoff {
    private long value;
    private final Mutex mutex = new Mutex();

    private static boolean trySwap(CellUsingReorderedBackoff a,
                                   CellUsingReorderedBackoff b)
            throws InterruptedException {
        boolean success = false;

        if (a.mutex.attempt(0)) {
            try {
                if (b.mutex.attempt(0)) {
                    try {
                        long t = a.value;
                        a.value = b.value;
                        b.value = t;
                        success = true;
                    } finally {
                        b.mutex.release();
                    }
                }
            } finally {
                a.mutex.release();
            }
        }

        return success;

    }

    void swapValue(CellUsingReorderedBackoff other) {
        if (this == other) return; // alias check required
        try {
            while (!trySwap(this, other) &&
                    !trySwap(other, this))
                Thread.sleep(100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
