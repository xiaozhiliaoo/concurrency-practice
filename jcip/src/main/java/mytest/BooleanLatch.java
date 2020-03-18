package mytest;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @packgeName: net.jcip.examples
 * @ClassName: BooleanLatch
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/6-13:24
 * @version: 1.0
 * @since: JDK 1.8
 */
public class BooleanLatch {

    private static class Sync extends AbstractQueuedSynchronizer{

        boolean isSignalled() { return getState() != 0; }

        protected int tryAcquireShared(int ignore) {
            return isSignalled() ? 1 : -1;
        }

        protected boolean tryReleaseShared(int ignore) {
            setState(1);
            return true;
        }
    }

    private final Sync sync = new Sync();

    public boolean isSignalled(){
        return sync.isSignalled();
    }

    public void signal(){
        sync.releaseShared(1);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }
}
