package chapter3;

import EDU.oswego.cs.dl.util.concurrent.Mutex;

import java.util.concurrent.locks.Lock;

/**
 * @author lili
 * @date 2020/5/30 20:38
 * @description
 * @notes
 */
public class WithLock {
    private final Lock lock;

    public WithLock(Lock m) {
        lock = m;
    }

    public void perform(Runnable r) throws InterruptedException {
        lock.lock();
        try {
            r.run();
        } finally {
            lock.unlock();
        }
    }
}
