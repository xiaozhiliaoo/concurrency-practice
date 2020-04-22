package org.lili;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * @author lili
 * @date 2020/4/21 20:12
 * @description 此例子验证first，two，three之间并行，且并行流并行执行任务，最后join合并结果，main 线程结束
 * @notes
 */
public class CompelteFutureTest {
    public static void main(String[] args) {
        CompelteFutureTest compelteFutureTest = new CompelteFutureTest();
//        compelteFutureTest.parllel();
        //compelteFutureTest.parllel2();
//        compelteFutureTest.parllel3();
        compelteFutureTest.parllel4();
    }

    private void parllel3() {
        CompletableFuture<Void> first = CompletableFuture.supplyAsync(this::first);
        CompletableFuture<Void> two = CompletableFuture.supplyAsync(this::two);
        CompletableFuture<Void> three = CompletableFuture.supplyAsync(this::three);
        CompletableFuture<Void> four = CompletableFuture.supplyAsync(this::four);
        CompletableFuture.allOf(first,two,three,four).join();
        System.out.println("main done...");
    }

    private void parllel4() {
        CompletableFuture<Void> first = CompletableFuture.supplyAsync(this::first);
        CompletableFuture<Void> two = CompletableFuture.supplyAsync(this::two);
        CompletableFuture<Void> three = CompletableFuture.supplyAsync(this::three);
        CompletableFuture<Void> four = CompletableFuture.supplyAsync(this::four);
        CompletableFuture.anyOf(first,two,three,four).join();
        System.out.println("main done...");
    }

    public Void combine(Void a, Void b) {
        return null;
    }

    public void parllel2() {
        CompletableFuture<Void> first = CompletableFuture.supplyAsync(this::first);
        CompletableFuture<Void> two = CompletableFuture.supplyAsync(this::two);
        CompletableFuture<Void> three = CompletableFuture.supplyAsync(this::three);
        first.acceptEither(two, System.out::println).acceptEither(three, System.out::println).join();
    }

    public void parllel() {
        System.out.println("main start...");
        CompletableFuture<Void> first = CompletableFuture.supplyAsync(this::first);
        CompletableFuture<Void> two = CompletableFuture.supplyAsync(this::two);
        CompletableFuture<Void> three = CompletableFuture.supplyAsync(this::three);
        CompletableFuture<Void> four = CompletableFuture.supplyAsync(this::four);
        CompletableFuture<Void> allCompletableFuture = first.thenCombine(two, this::combine).thenCombine(three, this::combine).thenCombine(four, this::combine);
        allCompletableFuture.join();
        System.out.println("main done...");
    }

    private Void first() {
        System.out.println("fisrt start....");
        List<String> stringList = Arrays.asList("first1", "first2", "first3");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringList.parallelStream().forEach(x -> System.out.println(x));
        System.out.println("fisrt end....");
        return null;
    }

    private Void two() {
        System.out.println("two start....");
        List<String> stringList = Arrays.asList("two1", "two2", "two3");
        stringList.parallelStream().forEach(x -> System.out.println(x));
        System.out.println("two end....");
        return null;
    }

    private Void three() {
        System.out.println("three start....");
        List<String> stringList = Arrays.asList("three1", "three2", "three3");
        stringList.parallelStream().forEach(x -> System.out.println(x));
        System.out.println("three end....");
        return null;
    }

    private Void four() {
        //new Thread(() -> System.out.println("ForkJoinPool 222222")).start();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.submit(() -> System.out.println("ForkJoinPool Test"));
        return null;
    }
}
