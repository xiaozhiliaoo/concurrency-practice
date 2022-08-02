package org.concurrency.parallel.functional.sample.parallelstream;

import org.concurrency.parallel.functional.sample.forkjoin.ForkJoinSumCalculator;

import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

import static org.concurrency.parallel.functional.sample.Utils.measurePerf;

public class ParallelStreamsHarness {

    public static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool();

    //一亿数字
    private static final long N = 1_1000_0000L ;


    public static void main(String[] args) {
        System.out.println("Iterative Sum done in: " + measurePerf(ParallelStreams::iterativeSum, N) + " ms");
        System.out.println("Sequential Stream iterate Sum done in: " + measurePerf(ParallelStreams::sequentialSum, N) + " ms");
        System.out.println("Parallel Stream iterate done in: " + measurePerf(ParallelStreams::parallelSum, N) + " ms");
        System.out.println("Sequential Stream rangeClosed done in: " + measurePerf(ParallelStreams::rangedSum, N) + " ms");
        System.out.println("Parallel Stream rangeClosed done in: " + measurePerf(ParallelStreams::parallelRangedSum, N) + " ms");
        System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, N) + " ms");
        System.out.println("SideEffect sum done in: " + measurePerf(ParallelStreams::sideEffectSum, N) + " ms");
        System.out.println("SideEffect parallel sum done in: " + measurePerf(ParallelStreams::sideEffectParallelSum, N) + " ms");
    }


}
