package mytest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @packgeName: net.jcip.examples
 * @ClassName: BoundedBuffer2
 * @copyright: CopyLeft
 * @description:<描述>  来自 jdk  Interface Condition 里面的demo
 * @author: lili
 * @date: 2017/10/6-14:49
 * @version: 1.0
 * @since: JDK 1.8
 */
public class BoundedBuffer2 {

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    final Object[] items = new Object[100];
    int putptr,takeptr,count;

    /**
     * 满的话阻塞，空的话放入，边界长度100
     * @param x
     * @throws InterruptedException
     */
    public void put(Object x)throws InterruptedException{
        //疑问：多个线程去put的时候会阻塞，满的时候也会阻塞住吗？？？
        lock.lock();
        try {
            while (count == items.length){
                notFull.await();
            }
            items[putptr] =  x;
            if(++putptr == items.length){
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 空的话阻塞，满的话从中获取
     * @return
     * @throws InterruptedException
     */
    public Object take() throws InterruptedException{
        lock.lock();
        try {
            while (count == 0) {
                notEmpty.await();
            }
            Object x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BoundedBuffer2 b = new BoundedBuffer2();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    for (int i = 0; i < 10; i++) {
                        b.put(i);
                        System.out.println(Thread.currentThread().getName()+" 放入 "+ i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        },"Producer1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    for (int i = 0; i < 100; i++) {
                        b.put(i);
                        System.out.println(Thread.currentThread().getName()+" 放入 "+ i);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        },"Producer2").start();

        /*try {
            for (int i = 0; i < 99; i++) {
                b.take();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
