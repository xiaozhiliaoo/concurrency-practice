package base;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author lili
 * @date 2020/4/29 11:56
 * @description https://docs.oracle.com/javase/8/docs/api/index.html?java/util/concurrent/locks/LockSupport.html
 * @notes
 */
public class FIFOMutex {
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedQueue<>();

}
