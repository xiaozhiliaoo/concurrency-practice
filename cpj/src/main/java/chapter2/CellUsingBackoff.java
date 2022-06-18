package chapter2;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2022/6/19 2:02
 */
public class CellUsingBackoff {
    private long value;
    private final Mutex mutex = new Mutex();

    void swapValue(CellUsingBackoff other) {
        if (this == other) return; // alias check required
        for (; ; ) {
            try {
                mutex.acquire();

                try {
                    if (other.mutex.attempt(0)) {
                        try {
                            long t = value;
                            value = other.value;
                            other.value = t;
                            return;
                        } finally {
                            other.mutex.release();
                        }
                    }
                } finally {
                    mutex.release();
                }
                ;

                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
