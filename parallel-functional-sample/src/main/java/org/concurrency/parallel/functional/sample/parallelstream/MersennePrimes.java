package org.concurrency.parallel.functional.sample.parallelstream;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

/**
 * 计算前20个梅森素数
 * <p>
 * code from effective java item48
 *
 * @author lili
 * @date 2022/8/3 10:41
 */
public class MersennePrimes {
    @Test
    public void sequential() {
        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    /**
     * steam iterate/limit pipeline不能提升性能
     * 1.steam不知道如何并行执行该pipeline
     * 2.源头和limit导致性能差
     * 3.limit策略，不可预知性，额外多处理几个元素，放弃不需要结果，每查找一个素数是前面2倍。
     */
    @Test
    public void parallel() {
        primes().parallel()
                .map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50))
                .limit(20)
                .forEach(System.out::println);
    }

    public static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, BigInteger::nextProbablePrime);
    }
}
