package org.example.lili;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lili
 * @date 2020/4/19 23:56
 * @description
 * @notes
 */
public class CompletionServiceTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ExecutorCompletionService<Object> completionService = new ExecutorCompletionService<>(service);

    }
}
