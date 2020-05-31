package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2020/5/30 20:49
 * @description
 * @notes
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