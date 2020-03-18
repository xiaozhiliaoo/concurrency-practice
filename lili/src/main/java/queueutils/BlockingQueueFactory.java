package queueutils;

import java.util.Comparator;
import java.util.concurrent.*;

/**
 * Created by lili on 2017/7/23
 */
public interface BlockingQueueFactory<T> extends QueueFactory<T> {
    public BlockingQueue<T> createQueue();

    public static class ArrayBlockingQueueFactory<T>  extends AbstractQueueFactory<T> implements BlockingQueueFactory<T>{

        private int capacity = Integer.MAX_VALUE;
        private boolean fair = false;

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public void setFair(boolean fair) {
            this.fair = fair;
        }

        public boolean isFair(){
            return this.fair;
        }

        @Override
        public ArrayBlockingQueue<T> createQueue() {
            if(this.initialContents == null || this.initialContents.isEmpty()){
                return new ArrayBlockingQueue<T>(capacity,fair);
            }else {
                if(this.initialContents.size() > capacity){
                    throw new IllegalStateException("超出队列最大长度");
                }else{
                    ArrayBlockingQueue<T> queue = new ArrayBlockingQueue<T>(this.capacity,this.fair);
                    queue.addAll(initialContents);
                    return queue;
                }
            }
        }
    }


    public static class DelayQueueFactoryL<T extends Delayed> extends AbstractQueueFactory<T>  implements BlockingQueueFactory<T> {
        public DelayQueue<T> createQueue() {
            if (this.initialContents == null || this.initialContents.isEmpty()) {
                return new DelayQueue<T>();
            } else {
                return new DelayQueue<T>(this.initialContents);
            }
        }
    }

    public static class LinkedBlockingQueueFactory<T> extends AbstractQueueFactory<T>  implements BlockingQueueFactory<T> {

        public LinkedBlockingQueue<T> createQueue() {
            if (this.initialContents == null || this.initialContents.isEmpty()) {
                return new LinkedBlockingQueue<T>(capacity);
            } else {
                if (this.initialContents.size() > this.capacity) {
                    throw new IllegalStateException("超出队列最大长度");
                } else {
                    LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>(capacity);
                    queue.addAll(this.initialContents);
                    return queue;
                }
            }
        }


        private int capacity = Integer.MAX_VALUE;


        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getCapacity() {
            return this.capacity;
        }
    }

    public static class PriorityBlockingQueueFactory<T> extends AbstractQueueFactory<T> implements BlockingQueueFactory<T> {
        public PriorityBlockingQueue<T> createQueue() {
            if (comparator == null) {
                if (this.initialContents == null || this.initialContents.isEmpty()) {
                    return new PriorityBlockingQueue<T>(initialCapacity);
                } else {
                    return new PriorityBlockingQueue<T>(this.initialContents);
                }
            } else {
                PriorityBlockingQueue<T> queue = new PriorityBlockingQueue<T>(initialCapacity, comparator);
                if ( !(this.initialContents == null || this.initialContents.isEmpty()) ) {
                    queue.addAll(this.initialContents);
                }
                return queue;
            }
        }

        private int initialCapacity = 11;


        public int getInitialCapacity() {
            return this.initialCapacity;
        }


        public void setInitialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
        }


        private Comparator<? super T> comparator;


        public Comparator<? super T> getComparator() {
            return this.comparator;
        }


        public void setComparator(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

    }

    public static class SynchronousQueueFactory<T> implements BlockingQueueFactory<T> {
        public SynchronousQueue<T> createQueue() {
            return new SynchronousQueue<T>(this.fair);
        }


        private boolean fair = false;


        public boolean isFair() {
            return this.fair;
        }

        public void setFair(boolean fair) {
            this.fair = fair;
        }
    }
}