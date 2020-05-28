package net.jcip.examples;

import java.util.concurrent.*;

/**
 * InterruptBorrowedThread
 * <p/>
 * Scheduling an interrupt on a borrowed thread
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimedRun1 {

    private static final ScheduledExecutorService cancelExec = Executors.newScheduledThreadPool(1);

    public static void timedRun(Runnable r, long timeout, TimeUnit unit) {
        final Thread taskThread = Thread.currentThread();
        System.out.println(taskThread.getName());
        cancelExec.schedule(new Runnable() {
            public void run() {
                taskThread.interrupt();
            }
        }, timeout, unit);
        r.run();
    }

    public static void main(String[] args) {
        timedRun(() -> System.out.println(34), 5L, TimeUnit.MILLISECONDS);
    }
}
