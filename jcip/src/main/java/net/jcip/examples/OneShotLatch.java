package net.jcip.examples;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * OneShotLatch
 * <p/>
 * Binary latch using AbstractQueuedSynchronizer
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class OneShotLatch {
    //one shot 只有一次
    private final Sync sync = new Sync();

    public void signal() {
        //
        sync.releaseShared(0);
    }

    public void await() throws InterruptedException {
        //0代表打开，获取0状态，
        sync.acquireSharedInterruptibly(0);
    }

    private class Sync extends AbstractQueuedSynchronizer {

        protected int tryAcquireShared(int ignored) {
            // Succeed if latch is open (state == 1), else fail
            return (getState() == 1) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int ignored) {
            setState(1); // Latch is now open
            return true; // Other threads may now be able to acquire

        }
    }

    public static void main(String[] args) throws InterruptedException {
//        OneShotLatch latch = new OneShotLatch();
//        latch.await();

        ReentrantLock lock = new ReentrantLock();
        //一直可以获取，证明可重入
//        lock.lock();
//        lock.lock();
//        lock.lock();

        //
//        new Thread(()->lock.lock()).start();

//        Debug
//        Semaphore semaphore = new Semaphore(2);
//        semaphore.acquire();
//        System.out.println("acquire1");
//        semaphore.acquire();
//        System.out.println("acquire2");
//        semaphore.acquire();
//        System.out.println("acquire3");

        ReentrantLock lock1 = new ReentrantLock();
        //lock1.unlock();
        lock1.lock();
        System.out.println("lock1");
        lock1.lock();
        System.out.println("lock2");
        lock1.lock();
        System.out.println("lock3");
        lock1.lock();
        System.out.println("lock4");

        AtomicInteger integer = new AtomicInteger(0);
        //只能被一次修改，初始值0 第一次被更新为1  后面两次不会被更新了
        System.out.println(integer.compareAndSet(0, 1));
        System.out.println(integer.compareAndSet(0, 1));
        System.out.println(integer.compareAndSet(0, 1));


    }
}
