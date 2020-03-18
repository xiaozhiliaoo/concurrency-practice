package queueutils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by lili on 2017/7/23
 */
public interface QueueFactory<T> {
    public Queue<T> createQueue();

    public static abstract class AbstractQueueFactory<T>{
        protected Collection<? extends T> initialContents;

        public Collection<? extends T> getInitialContents() {
            return initialContents;
        }

        public void setInitialContents(Collection<? extends T> initialContents) {
            this.initialContents = initialContents;
        }
    }

    public static class LinkedListFactory<T> extends AbstractQueueFactory<T> implements QueueFactory<T>{

        @Override
        public Queue<T> createQueue() {
            if(this.initialContents==null || this.initialContents.isEmpty()){
                return new LinkedList<T>();
            }else{
                return new LinkedList<T>(initialContents);
            }
        }
    }

    public static class ConcurrentLinkedQueueFactory<T> extends AbstractQueueFactory<T> implements QueueFactory<T> {
        public ConcurrentLinkedQueue<T> createQueue() {
            if (this.initialContents == null || this.initialContents.isEmpty()) {
                return new ConcurrentLinkedQueue<T>();
            } else {
                return new ConcurrentLinkedQueue<T>(this.initialContents);
            }
        }
    }


    public static class PriorityQueueFactory<T> extends AbstractQueueFactory<T> implements QueueFactory<T>{

        private Comparator<? super T> comparator;

        private int initialCapacity = 11;

        public int getInitialCapacity() {
            return initialCapacity;
        }

        public void setInitialCapacity(int initialCapacity) {
            this.initialCapacity = initialCapacity;
        }

        public Comparator<? super T> getComparator() {
            return comparator;
        }

        public void setComparator(Comparator<? super T> comparator) {
            this.comparator = comparator;
        }

        @Override
        public PriorityQueue<T> createQueue() {
            if(comparator == null){
                if(this.initialContents == null || this.initialContents.isEmpty()){
                    return new PriorityQueue<T>(initialCapacity);
                }else{
                    return new PriorityQueue<T>(initialContents);
                }
            }else{
                PriorityQueue<T> queue = new PriorityQueue<T>(initialCapacity,comparator);
                queue.addAll(this.initialContents);
                return queue;
            }
        }
    }


}
