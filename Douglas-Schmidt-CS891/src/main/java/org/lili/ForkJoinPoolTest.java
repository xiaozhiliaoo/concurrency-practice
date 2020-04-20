package org.lili;

import java.util.concurrent.ForkJoinPool;

/**
 * @author lili
 * @date 2020/4/15 23:24
 * @description
 * @notes
 */
public class ForkJoinPoolTest {
    public static void main(String[] args) {
        System.out.println(ForkJoinPool.getCommonPoolParallelism());

        int numberOfThreads = 10;
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(numberOfThreads));
        System.out.println(ForkJoinPool.getCommonPoolParallelism());

//        ForkJoinPool.managedBlock()

    }
}
