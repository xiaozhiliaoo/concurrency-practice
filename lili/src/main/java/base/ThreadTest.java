package base;

import java.util.concurrent.locks.LockSupport;

/**
 * @author lili
 * @date 2020/4/29 11:32
 * @description 线程里面有三个方法被移除。resume，suspend，stop
 * wait和sleep区别，wait释放锁，如果获取锁了，就不会阻塞住，sleep
 * @notes
 */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        o.wait();
        //Thread.currentThread().suspend();
        Thread thread = Thread.currentThread();
        Thread.dumpStack();
        System.out.println(thread.countStackFrames());
        LockSupport.park();
        LockSupport.unpark(thread);
    }
}
