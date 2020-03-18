package mytest;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @packgeName: mytest
 * @ClassName: PausableThreadPoolExecutor
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/8-14:33
 * @version: 1.0
 * @since: JDK 1.8
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {

    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();
    private Condition unpaused = pauseLock.newCondition();

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        System.out.println("准备执行任务....");
        pauseLock.lock();
        try {
            while (isPaused){
                unpaused.await();
            }
        } catch (InterruptedException e) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause(){
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause(long time , TimeUnit unit){
        try {
            pauseLock.lock(); //如果去除这句话，会有问题 IllegalMonitorStateException 释放锁的时候报错
            //pauseLock.tryLock(time,unit);
            unit.sleep(time);
            isPaused = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pauseLock.unlock();
            resume();
            //isPaused = false; //非线程安全的方法
        }
    }

    public void resume(){
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        System.out.println("任务执行结束...");
    }

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static void main(String[] args) {
        Task task= new Task();
        PausableThreadPoolExecutor es = new PausableThreadPoolExecutor(1,1,0L,TimeUnit.SECONDS,new LinkedBlockingQueue<>());
        //停止10s后运行任务
        es.pause(5, TimeUnit.SECONDS);//5s
        es.execute(task);

        es.shutdown();
    }
}
