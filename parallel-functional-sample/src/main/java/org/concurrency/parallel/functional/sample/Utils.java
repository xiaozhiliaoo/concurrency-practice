package org.concurrency.parallel.functional.sample;

import java.util.function.Function;

/**
 * @author lili
 * @date 2022/8/2 20:53
 */
public final class Utils {
    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;
        long start = System.currentTimeMillis();
        R result = f.apply(input);
        return (System.currentTimeMillis() - start);
    }

}
