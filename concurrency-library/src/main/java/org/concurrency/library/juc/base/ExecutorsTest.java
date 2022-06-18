package org.concurrency.library.juc.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lili
 * @date 2020/5/25 10:54
 * @description
 * @notes
 */
public class ExecutorsTest {
    private static void printOne() {
        System.out.println("do Print One");
        printTwo();
    }
    private static void printTwo() {
        System.out.println("do Print Two");
        printThree();
    }
    private static void printThree() {
        System.out.println("do Print Three");
    }
    public static void main(String[] args) throws Exception {
        ThreadGroup group = new ThreadGroup("thread-Group");
        Thread thread = new Thread(group, ()-> {
            printOne();
        },"myThread",1);
        thread.start();


        ExecutorService service = Executors.newCachedThreadPool();
//        service.shutdown();
        //service.awaitTermination(1, TimeUnit.MILLISECONDS);
        service.submit(()-> System.out.println(1));
        System.out.println(service.isShutdown());
    }
}
