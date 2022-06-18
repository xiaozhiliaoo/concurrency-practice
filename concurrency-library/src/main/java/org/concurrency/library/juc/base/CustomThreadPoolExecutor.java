package org.concurrency.library.juc.base;

import java.util.concurrent.*;

/**
 * @author lili
 * @date 2020/5/25 22:48
 * @description
 * @notes
 */
public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    static class CustomTask<V> implements RunnableFuture<V> {

        public CustomTask() {
        }

        public CustomTask(Runnable r, V v) {

        }

        @Override
        public void run() {

        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            return null;
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return null;
        }
    }

    @Override
    protected <V> RunnableFuture<V> newTaskFor(Runnable r, V v) {
        return new CustomTask<V>(r, v);
    }
}
