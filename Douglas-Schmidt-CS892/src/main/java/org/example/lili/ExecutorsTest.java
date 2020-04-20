package org.example.lili;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author lili
 * @date 2020/4/19 17:47
 * @description
 * @notes
 */
public class ExecutorsTest {
    public static void main(String[] args) {
        ThreadFactory threadFactory = Executors.privilegedThreadFactory();
        ThreadFactory threadFactory1 = Executors.defaultThreadFactory();
        ExecutorService service = Executors.newCachedThreadPool(threadFactory);
        Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    }
}
