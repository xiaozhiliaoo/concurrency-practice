package org.lili;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 异构任务并行化
 *
 * @author lili
 * @date 2022/7/17 17:34
 */
public class HeterogeneousTaskParallel {

    ExecutorService executor = Executors.newCachedThreadPool();
    //三个并行无依赖的任务


    private static List<String> task1Cost15s(int number) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String name = Thread.currentThread().getName();
        System.out.println("task1 start...." + name);
        try {
            Thread.sleep(15000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> result = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            result.add(String.valueOf(i));
        }
        System.out.println("task1 result. cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        return result;
    }

    private static Map<Integer, String> task2Cost5s(int number) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String name = Thread.currentThread().getName();
        System.out.println("task2 start...." + name);
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();

        }
        Map<Integer, String> result = new HashMap<>();
        for (int i = 0; i < number; i++) {
            result.put(i, String.valueOf(i));
        }
        System.out.println("task2 result. cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        return result;
    }

    private static Integer task3Cost10s() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String name = Thread.currentThread().getName();
        System.out.println("task3 start...." + name);
        int result = Integer.MIN_VALUE;
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("task3 result. cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        return result;
    }

    @Test
    @DisplayName("任务耗时30s")
    void t2() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<List<String>> t1 = CompletableFuture.supplyAsync(() -> task1Cost15s(10000), executor);
        t1.get();
        CompletableFuture<Map<Integer, String>> t2 = CompletableFuture.supplyAsync(() -> task2Cost5s(10000), executor);
        t2.get();
        CompletableFuture<Integer> t3 = CompletableFuture.supplyAsync(() -> task3Cost10s(), executor);
        t3.get();
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }

    @Test
    @DisplayName("任务耗时15s")
    void t3() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<List<String>> t1 = CompletableFuture.supplyAsync(() -> task1Cost15s(10000), executor);
        CompletableFuture<Map<Integer, String>> t2 = CompletableFuture.supplyAsync(() -> task2Cost5s(10000), executor);
        CompletableFuture<Integer> t3 = CompletableFuture.supplyAsync(() -> task3Cost10s(), executor);
        t1.get();
        t2.get();
        t3.get();
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }

    @Test
    @DisplayName("测试线程id")
    void t31() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Integer join = CompletableFuture.supplyAsync(() -> task1Cost15s(10000), executor)
                .thenCompose(x -> {
                    return CompletableFuture.supplyAsync(() -> task2Cost5s(10000), executor);
                })
                .thenCompose(x -> {
                    return CompletableFuture.supplyAsync(() -> task3Cost10s(), executor);
                })
                .join();
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }


    @Test
    void t6_thenCombine() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<List<String>> t1 = CompletableFuture.supplyAsync(() -> task1Cost15s(10));
        CompletableFuture<Map<Integer, String>> t2 = CompletableFuture.supplyAsync(() -> task2Cost5s(5));
        t1.thenCombine(t2, (x, y) -> {
            System.out.println("x:" + x);
            System.out.println("y:" + y);
            return x.size() + y.size();
        }).whenComplete((x, y) -> {
            System.out.println("complete");
        }).thenAccept(x -> System.out.println("结果是：" + x));
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }


    @Test
    void t7_thenCompose() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<List<String>> t1 = CompletableFuture.supplyAsync(() -> task1Cost15s(10));
        t1.thenCompose(x -> {

            System.out.println(x + " thenCompose");
            return CompletableFuture.supplyAsync(() -> task2Cost5s(x.size()));
        }).whenComplete((x, y) -> {
            System.out.println("complete");
        });
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }


    @Test
    @DisplayName("任务耗时15s")
    void t5() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<List<String>> t1 = executor.submit(() -> task1Cost15s(10000));
        Future<Map<Integer, String>> t2 = executor.submit(() -> task2Cost5s(10000));
        Future<Integer> t3 = executor.submit(() -> task3Cost10s());
        Integer integer = t3.get();
        System.out.println("t3:" + integer);
        Map<Integer, String> integerStringMap = t2.get();
        System.out.println("t2:" + integerStringMap.size());
        List<String> strings = t1.get();
        System.out.println("t1:" + strings.size());
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }

    @Test
    void t4() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        CompletableFuture<List<String>> t1 = CompletableFuture.supplyAsync(() -> task1Cost15s(10000), executor);
        CompletableFuture<Map<Integer, String>> t2 = CompletableFuture.supplyAsync(() -> task2Cost5s(10000), executor);
        CompletableFuture<Integer> t3 = CompletableFuture.supplyAsync(() -> task3Cost10s(), executor);
        t1.whenComplete((x, y) -> {
            System.out.println("t1完成");
        });
        t2.whenComplete((x, y) -> {
            System.out.println("t2完成");
        });
        t3.whenComplete((x, y) -> {
            System.out.println("t3完成");
        });
        CompletableFuture.allOf(t1, t2, t3).whenComplete((x, y) -> {
            System.out.println("所有任务完成:" + x);
        }).join();
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        Thread.sleep(111111);
    }


    @Test
    void t1() throws Exception {
        CompletableFuture<List<String>> one = CompletableFuture.supplyAsync(() -> task1Cost15s(10000));
        CompletableFuture<Map<Integer, String>> two = CompletableFuture.supplyAsync(() -> task2Cost5s(70000));
        CompletableFuture<Integer> three = CompletableFuture.supplyAsync(() -> task3Cost10s());

        Stopwatch stopwatch = Stopwatch.createStarted();

        one.whenComplete((x, y) -> {
            System.out.println("任务1完成");
        });
        System.out.println("任务1之后");
        two.whenComplete((x, y) -> {
            System.out.println("任务2完成");
        });
        System.out.println("任务2之后");

        three.whenComplete((x, y) -> {
            System.out.println("任务3完成");
        });
        System.out.println("任务3之后");

        //等待三个完成
        CompletableFuture.allOf(one, two, three).whenComplete((x, y) -> {
            System.out.println("所有任务完成:" + x);
        }).join();
        //获取结果
        List<String> task1Res = one.get();
        Map<Integer, String> task2Res = two.get();
        Integer task3Res = three.get();
        System.out.println("t1 result:" + task1Res.size());
        System.out.println("t2 result:" + task2Res.size());
        System.out.println("t3 result:" + task3Res);
        System.out.println("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
    }

}
