package mytest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @packgeName: mytest
 * @ClassName: ThreadPoolTest
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/8-11:35
 * @version: 1.0
 * @since: JDK 1.8
 */
class Task implements Runnable{

    @Override
    public void run() {
        System.out.println("I am execute job");
    }
}


public class ThreadPoolTest {
    public static void main(String[] args) {
        Task t = new Task();
        ExecutorService es = Executors.newCachedThreadPool();
//        Executors.unconfigurableExecutorService(es).
        for (int i = 0; i < 10; i++) {
            es.execute(t);
        }
//        es.shutdown();
    }
}
