package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:11
 */
public class OwnedPartWithGuard {                      // Code sketch
    protected boolean cond = false;
    final Object lock;

    OwnedPartWithGuard(Object owner) {
        lock = owner;
    }

    void await() throws InterruptedException {
        synchronized (lock) {
            while (!cond)
                lock.wait();
            // ...
        }
    }

    void signal(boolean c) {
        synchronized (lock) {
            cond = c;
            lock.notifyAll();
        }
    }
}
