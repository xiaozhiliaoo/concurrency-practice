package net.jcip.examples;

import net.jcip.annotations.*;

/**
 * BoundedBuffer
 * <p/>
 * Bounded buffer using condition queues
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BoundedBuffer<V> extends BaseBoundedBuffer<V> {
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer() {
        this(100);
    }

    public BoundedBuffer(int size) {
        super(size);
    }

    /**
     * 只有队列不满的时候，才会阻塞，如果队列满， 那么不会阻塞了。
     *
     * @param v
     * @throws InterruptedException
     */
    // BLOCKS-UNTIL: not-full
    public synchronized void put(V v) throws InterruptedException {
        while (isFull()) { //这里的while不是if
            //释放锁，挂起当前线程，等待别人通知。释放锁这么理解：如果没有释放锁，那么别的线程将进入不了方法
            //也就不会等待
            //一直等着缓存不满
            wait();
        }
        //修改状态变量
        doPut(v);
        //通知其他线程(等待缓存为非空的),缓存已经不空了,等待的线程可以拿数据了
        notifyAll();
    }

    /**
     * 解读：当多个线程去take时候，如果缓冲有数据，那么该线程将获取锁，其他线程再次take时候
     * 会被阻塞，挂起，当take完之后，别的线程才可以继续take。但是如果缓冲区没有数据，当前线程
     * 将释放锁，并且请求OS挂起当前线程，从而别的线程可以继续take，或者put，也就是可以修改状态，
     * 当wait线程醒来时候，将重新获取锁。一个是挂起自己，一个是挂起别人。
     * 为什么while条件谓词？
     * 为什么notifyAll()?什么时候使用notify()?
     *
     * @return
     * @throws InterruptedException
     */
    // BLOCKS-UNTIL: not-empty
    public synchronized V take() throws InterruptedException {
        while (isEmpty()) {
            //一直等着缓存不为空,释放对象锁，自己被阻塞
            wait();
        }
        V v = doTake();
        //通知其他线程(等待缓存非满的)：BoundedBuffer已经不满了，你们可以put数据了
        notifyAll();
        return v;
    }

    // BLOCKS-UNTIL: not-full
    // Alternate form of put() using conditional notification
    public synchronized void alternatePut(V v) throws InterruptedException {
        while (isFull()) {
            wait();
        }
        boolean wasEmpty = isEmpty();
        doPut(v); //从空到非空
        if (wasEmpty) {
            //仅当空变为非空时候才发出通知
            notifyAll();
        }
    }

    public static void main(String[] args) {
        BoundedBuffer<String> boundedBuffer = new BoundedBuffer<>(3);

        //一个线程 boundedBuffer.put("11");
        // boundedBuffer.put("22");
        // boundedBuffer.take(); 都是同一把锁
        new Thread(() -> {
            try {
                boundedBuffer.put("11");
                boundedBuffer.put("22");
                boundedBuffer.put("33");
                boundedBuffer.put("44");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            try {
                System.out.println("take now:" + boundedBuffer.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
