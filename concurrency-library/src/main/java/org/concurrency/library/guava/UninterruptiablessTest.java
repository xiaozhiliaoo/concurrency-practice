package org.concurrency.library.guava;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import java.time.Duration;
import java.util.concurrent.*;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
 * @author lili
 * @date 2020/5/30 19:17
 * @description
 * @notes
 */
public class UninterruptiablessTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        SimpleTimeLimiter timeLimiter = SimpleTimeLimiter.create(service);
//        try {
//            timeLimiter.runWithTimeout(() -> {
//                //            try {
//                //                TimeUnit.SECONDS.sleep(20);
//                //            } catch (InterruptedException e) {
//                //                e.printStackTrace();
//                //            }
//                Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(20));
//                System.out.println("time done.");
//            }, Duration.ofSeconds(1));
//        } catch (TimeoutException e) {
//            //e.printStackTrace();
//        } catch (InterruptedException e) {
//            //e.printStackTrace();
//        }


        try {
            timeLimiter.runWithTimeout(() -> {
                BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
                try {
                    blockingQueue.put("1");
                    blockingQueue.put("2");
                    blockingQueue.put("3");
                    //blockingQueue.put("4");
                    System.out.println(blockingQueue);
                } catch (InterruptedException e) {
                    System.out.println("blockingQueue InterruptedException ... ");
                    e.printStackTrace();
                }
            }, Duration.ofNanos(1));
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
