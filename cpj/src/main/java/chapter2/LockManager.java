package chapter2;

import EDU.oswego.cs.dl.util.concurrent.Sync;

/**
 * @author lili
 * @date 2020/5/30 20:56
 * @description
 * @notes
 */
public class LockManager {
    protected void sortLocks(Sync[] lock) {
    }

    public void runWithinLocks(Runnable op, Sync[] locks) throws InterruptedException {
        sortLocks(locks);
        int lastLocked = -1;
        InterruptedException caugth = null;

        try {
            for (int i = 0; i < locks.length; ++i) {
                locks[i].acquire();
                lastLocked = i;
            }
            op.run();
        } catch (InterruptedException ie) {
            caugth = ie;
        } finally {
            for (int j = lastLocked; j >= 0; --j) {
                locks[j].release();
            }
            if (caugth != null) {
                throw caugth;
            }
        }
    }
}
