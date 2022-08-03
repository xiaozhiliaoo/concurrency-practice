package org.concurrency.parallel.functional.sample.parallelstream;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.LongStream;

/**
 * 求π(n),素数数量少于或者等于N
 * <p>
 * code from effective java item48
 *
 * @author lili
 * @date 2022/8/3 10:33
 */
public class CalcPi {

    private static final long N = 1_0000_000;

    @Test
    public void parallel() {
        System.out.println(piParallel(N));
    }

    @Test
    public void sequential() {
        System.out.println(piSequential(N));
    }


    public static long piParallel(long n) {
        return LongStream.rangeClosed(2, n)
                .parallel()
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }


    public static long piSequential(long n) {
        return LongStream.rangeClosed(2, n)
                .mapToObj(BigInteger::valueOf)
                .filter(i -> i.isProbablePrime(50))
                .count();
    }
}
