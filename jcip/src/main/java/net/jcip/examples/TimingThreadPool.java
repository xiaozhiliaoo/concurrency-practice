package net.jcip.examples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.logging.*;

/**
 * TimingThreadPool
 * <p/>
 * Thread pool extended with logging and timing
 *
 * @author Brian Goetz and Tim Peierls
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    public TimingThreadPool() {
//        super(1, 1, 0L, TimeUnit.SECONDS, null);
        super(1, 1, 0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
    private final Logger log = Logger.getLogger("TimingThreadPool");
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTime = new AtomicLong();

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
//        log.fine(String.format("Thread %s: start %s", t, r));
        System.out.println(String.format("Thread %s: start %s", t, r));
        startTime.set(System.nanoTime());
    }

    protected void afterExecute(Runnable r, Throwable t) {
        try {
            long endTime = System.nanoTime();
            long taskTime = endTime - startTime.get();
            numTasks.incrementAndGet();
            totalTime.addAndGet(taskTime);
//            log.fine(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
            System.out.println(String.format("Thread %s: end %s, time=%dns", t, r, taskTime));
        } finally {
            super.afterExecute(r, t);
        }
    }

    protected void terminated() {
        try {
//            log.info(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
            System.out.println(String.format("Terminated: avg time=%dns", totalTime.get() / numTasks.get()));
        } finally {
            super.terminated();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = new TimingThreadPool();
        for (int i = 0; i < 10; i++) {
            executor.execute(new TaskDemo());

        }
        executor.shutdown();

//        p.terminated();
    }
}
