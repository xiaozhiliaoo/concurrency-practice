package chapter2;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2022/6/19 2:01
 */
public class WithMutex {
    private final Mutex mutex;

    public WithMutex(Mutex m) {
        mutex = m;
    }

    public void perform(Runnable r) throws InterruptedException {
        mutex.acquire();
        try {
            r.run();
        } finally {
            mutex.release();
        }
    }
}
