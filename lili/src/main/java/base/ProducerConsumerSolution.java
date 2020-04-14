package base;

import java.util.Vector;

/**
 * @author lili
 * @date 2020/4/14 18:41
 * @description
 * @notes
 */
public class ProducerConsumerSolution {


    public static void main(String[] args) {
        Vector sharedQueue = new Vector();
        Vector sharedQueue2 = new Vector();
        int size = 5;
        Thread prodThread = new Thread(new Producer(sharedQueue, size), "Producer");
        Thread consThread = new Thread(new Consumer(sharedQueue2, size), "Consumer");
        prodThread.start();
        consThread.start();
    }

    static class Producer implements Runnable {
        private final Vector sharedQueue;
        private final int SIZE;

        public Producer(Vector sharedQueue, int size) {
            this.sharedQueue = sharedQueue;
            this.SIZE = size;
        }

        @Override
        public void run() {
            for (int i = 0; i < 7; i++) {
                System.out.println("Producer" + i);
                try {
                    produce(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void produce(int i) throws InterruptedException {
            while (sharedQueue.size() == SIZE) {
                synchronized (sharedQueue) {
                    synchronized (sharedQueue) {
                        System.out.println("Queue is full " + Thread.currentThread().getName()
                                + " is waiting , size: " + sharedQueue.size());
                        sharedQueue.wait();
                    }
                }
            }

            synchronized (sharedQueue) {
                sharedQueue.add(i);
                sharedQueue.notifyAll();
            }
        }
    }

    static class Consumer implements Runnable {

        private final Vector sharedQueue;
        private final int SIZE;

        public Consumer(Vector sharedQueue, int size) {
            this.sharedQueue = sharedQueue;
            this.SIZE = size;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Consumed: " + consume());
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                }

            }
        }

        private int consume() throws InterruptedException {
            //wait if queue is empty
            while (sharedQueue.isEmpty()) {
                synchronized (sharedQueue) {
                    System.out.println("Queue is empty " + Thread.currentThread().getName()
                            + " is waiting , size: " + sharedQueue.size());

                    sharedQueue.wait();
                }
            }

            //Otherwise consume element and notify waiting producer
            synchronized (sharedQueue) {
                sharedQueue.notifyAll();
                return (Integer) sharedQueue.remove(0);
            }
        }
    }
}
