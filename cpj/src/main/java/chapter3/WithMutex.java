package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

/**
 * @author lili
 * @date 2020/5/30 20:34
 * @description
 * @notes
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
