package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:22
 */
public class FIFOSemaphore extends Semaphore {


    protected final WaitQueue queue = new WaitQueue();

    public FIFOSemaphore(long initialPermits) {
        super(initialPermits);
    }

    public void acquire() throws InterruptedException {
        if (Thread.interrupted()) throw new InterruptedException();

        WaitNode node = null;

        synchronized (this) {
            if (permits > 0) {    // no need to queue
                --permits;
                return;
            } else {
                node = new WaitNode();
                queue.enq(node);
            }
        }

        // must release lock before node wait

        node.doWait();

    }

    public synchronized void release() {
        for (; ; ) {                // retry until success
            WaitNode node = queue.deq();

            if (node == null) {    // queue is empty
                ++permits;
                return;
            } else if (node.doNotify())
                return;

            // else node was already released due to
            //   interruption or time-out, so must retry
        }
    }


    // Queue node class. Each node serves as a monitor.

    protected static class WaitNode {
        boolean released = false;
        WaitNode next = null;

        synchronized void doWait() throws InterruptedException {
            try {
                while (!released)
                    wait();
            } catch (InterruptedException ie) {

                if (!released) {        // Interrupted before notified
                    // Suppress future notifications:
                    released = true;
                    throw ie;
                } else {                  // Interrupted after notified
                    // Ignore exception but propagate status:
                    Thread.currentThread().interrupt();
                }

            }
        }

        synchronized boolean doNotify() { // return true if notified

            if (released)             // was interrupted or timed out
                return false;
            else {
                released = true;
                notify();
                return true;
            }
        }

        synchronized boolean doTimedWait(long msecs)
                throws InterruptedException {
            return true;
            // similar
        }
    }

    // Standard linked queue class.
    // Used only when holding Semaphore lock.

    protected static class WaitQueue {
        protected WaitNode head = null;
        protected WaitNode last = null;

        protected void enq(WaitNode node) {
            if (last == null)
                head = last = node;
            else {
                last.next = node;
                last = node;
            }
        }

        protected WaitNode deq() {
            WaitNode node = head;
            if (node != null) {
                head = node.next;
                if (head == null) last = null;
                node.next = null;
            }
            return node;
        }
    }
}