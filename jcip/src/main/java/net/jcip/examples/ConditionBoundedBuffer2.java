package net.jcip.examples;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @packgeName: net.jcip.examples
 * @ClassName: ConditionBoundedBuffer2
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/9-21:30
 * @version: 1.0
 * @since: JDK 1.8
 */
public class ConditionBoundedBuffer2<T> {

    protected final Lock lock = new ReentrantLock();


    private final Condition notFull = lock.newCondition();


    private final Condition notEmpty = lock.newCondition();


    private static final int BUFFER_SIZE = 100;


    private final T[]  items = (T[])new Object[BUFFER_SIZE];


    private int tail,head,count;


    public void put(T x) throws InterruptedException{
        lock.lock();
        try {
            while (count == items.length){
                notFull.await(); //如果队列缓冲满了，那么等不满...await().....
            }
            items[tail] = x;
            if(++tail == items.length){
                tail = 0;
            }
            ++count;
            notEmpty.signal(); //此时候必须告诉大家，你们可以来拿了，现在不是空的
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException{
        lock.lock();
        try{
            while (count == 0){
                notEmpty.await(); //等到不是空的时候再去拿,等啊等......
            }
            T x = items[head];
            items[head] = null;
            if(++head == items.length){
                head = 0;
            }
            --count;
            notFull.signal(); //通知大家现在缓存不是满的，你们可以放东西了~~~
            return x;
        }finally {
            lock.unlock();
        }
    }

}
