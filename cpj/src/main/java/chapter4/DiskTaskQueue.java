package chapter4;

import EDU.oswego.cs.dl.util.concurrent.Semaphore;

/**
 * @author lili
 * @date 2022/6/19 10:49
 */
public class DiskTaskQueue {
    protected DiskTask thisSweep = null;
    protected DiskTask nextSweep = null;
    protected int currentCylinder = 0;

    protected final Semaphore available = new Semaphore(0);

    void put(DiskTask t) {
        insert(t);
        available.release();
    }

    DiskTask take() throws InterruptedException {
        available.acquire();
        return extract();
    }

    synchronized void insert(DiskTask t) {
        DiskTask q;
        if (t.cylinder >= currentCylinder) {   // determine queue
            q = thisSweep;
            if (q == null) {
                thisSweep = t;
                return;
            }
        } else {
            q = nextSweep;
            if (q == null) {
                nextSweep = t;
                return;
            }
        }
        DiskTask trail = q;            // ordered linked list insert
        q = trail.next;
        for (; ; ) {
            if (q == null || t.cylinder < q.cylinder) {
                trail.next = t;
                t.next = q;
                return;
            } else {
                trail = q;
                q = q.next;
            }
        }
    }

    synchronized DiskTask extract() { // PRE: not empty
        if (thisSweep == null) {           // possibly swap queues
            thisSweep = nextSweep;
            nextSweep = null;
        }
        DiskTask t = thisSweep;
        thisSweep = t.next;
        currentCylinder = t.cylinder;
        return t;
    }
}
