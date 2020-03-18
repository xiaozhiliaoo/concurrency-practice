package chapter4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by lili on 2017/4/28.
 */
public class BlockingQueueTest {

    public class Basket{

        BlockingQueue<String> basket = new LinkedBlockingDeque<>();
        public void produce() throws InterruptedException {
            basket.put("an apple");
        }
        public void consume() throws InterruptedException {
            basket.take();
        }

    }

    class Producer implements Runnable{
        private String instance;
        private Basket basket;

        public Producer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }

        @Override
        public void run() {
            while(true){
                System.out.println("生产者准备生产苹果：" + instance);
                try {
                    basket.produce();
                    System.out.println("生产者完毕");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }

        }
    }



    class Consumer implements Runnable{

        private String instance;
        private Basket basket;

        public Consumer(String instance, Basket basket) {
            this.instance = instance;
            this.basket = basket;
        }
        @Override
        public void run() {
            while(true){
                System.out.println("消费者消费苹果：" + instance);
                try {
                    basket.consume();
                    System.out.println("消费者消费完毕");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        }
    }


    public static void main(String[] args) {
        BlockingQueueTest test =  new BlockingQueueTest();
        Basket basket = test.new Basket();
        ExecutorService es = Executors.newCachedThreadPool();
        Producer producer = test.new Producer("生产者001", basket);
        Producer producer2 = test.new Producer("生产者002", basket);
        Consumer consumer = test.new Consumer("消费者001", basket);
        es.submit(producer);
        es.submit(producer2);
        es.submit(consumer);
    }
}
