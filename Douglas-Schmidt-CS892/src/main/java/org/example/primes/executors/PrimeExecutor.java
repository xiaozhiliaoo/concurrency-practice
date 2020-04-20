package org.example.primes.executors;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lili
 * @date 2020/4/19 18:52
 * @description
 * @notes
 */
public class PrimeExecutor {

    /**
     * Maximum random number value.
     */
    private static long MAX_VALUE = 1000000000L;

    private static long COUNT = 1L;

    /**
     * Maximum range of random numbers where range is
     * [MAX_VALUE - MAX_COUNT .. MAX_VALUE].
     */
    private static int MAX_COUNT = 1000;

    private static Executor mExecutor =
            Executors.newFixedThreadPool(Runtime
                    .getRuntime()
                    .availableProcessors());

    public static void main(String[] args) {
        new Random()
                // Generate "count" random between sMAX_VALUE - count
                // and sMAX_VALUE.
                .longs(COUNT, MAX_VALUE - COUNT, MAX_VALUE)

                // Convert each random number into a PrimeRunnable and
                // execute it.
                .forEach(randomNumber -> mExecutor.execute(new PrimeRunnable(randomNumber)));
    }
}
