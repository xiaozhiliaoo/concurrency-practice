package chapter4;

import java.util.Date;

/**
 * @author lili
 * @date 2022/6/19 10:32
 */
public class TimerDaemon {                               // Fragments

    static class TimerTask implements Comparable {
        final Runnable command;
        final long execTime;       // time to run at

        public int compareTo(Object x) {
            long otherExecTime = ((TimerTask) (x)).execTime;
            return (execTime < otherExecTime) ? -1 :
                    (execTime == otherExecTime) ? 0 : 1;
        }

        TimerTask(Runnable r, long t) {
            command = r;
            execTime = t;
        }
    }

    // a heap or list with methods that preserve
    // ordering with respect to TimerTask.compareTo

    static class PriorityQueue {
        void put(TimerTask t) {
        }

        TimerTask least() {
            return null;
        }

        void removeLeast() {
        }

        boolean isEmpty() {
            return true;
        }
    }

    protected final PriorityQueue pq = new PriorityQueue();

    public synchronized void executeAfterDelay(Runnable r, long t) {
        pq.put(new TimerTask(r, t + System.currentTimeMillis()));
        notifyAll();
    }

    public synchronized void executeAt(Runnable r, Date time) {
        pq.put(new TimerTask(r, time.getTime()));
        notifyAll();
    }

    // wait for and then return next task to run
    protected synchronized Runnable take()
            throws InterruptedException {
        for (; ; ) {
            while (pq.isEmpty())
                wait();
            TimerTask t = pq.least();
            long now = System.currentTimeMillis();
            long waitTime = now - t.execTime;
            if (waitTime <= 0) {
                pq.removeLeast();
                return t.command;
            } else
                wait(waitTime);
        }
    }

    public TimerDaemon() {
        activate();
    } // only one

    void activate() {
        // same as PlainWorkerThread except using above take method
    }
}
