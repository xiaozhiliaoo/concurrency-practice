package org.example.ex8;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lili
 * @date 2020/4/17 2:14
 * @description
 * @notes
 */
public class myex8 {
    public static void main(String[] args) {
        BigFraction unreduced = BigFraction.valueOf(new BigInteger("88888888544444"), new BigInteger("155555543345654"), false);
        Supplier<BigFraction> reduce = () -> BigFraction.reduce(unreduced);
        CompletableFuture.supplyAsync(reduce).thenApply(BigFraction::toMixedString).thenAccept(System.out::println);


        BigFraction unreduced2 = BigFraction.valueOf(new BigInteger("88888888544444"), new BigInteger("155555543345654"), false);

        //Nested CompletableFuture
        Function<BigFraction, CompletableFuture<CompletableFuture<BigFraction>>> function =
                unreduceds -> CompletableFuture.supplyAsync(() -> BigFraction.reduce(unreduced2))
                        .thenApply(reduced -> CompletableFuture.supplyAsync(() -> reduced.multiply(1)));

        Function<BigFraction, CompletableFuture<BigFraction>> function2 =
                unreduceds -> CompletableFuture.supplyAsync(() -> BigFraction.reduce(unreduced2))
                        .thenApplyAsync(reduced -> reduced.multiply(1));

        //two stage add
        BigFraction b1 = BigFraction.valueOf(new BigInteger("88888888544444"), new BigInteger("155555543345654"), false);
        BigFraction b2 = BigFraction.valueOf(new BigInteger("88888888544444"), new BigInteger("155555543345654"), false);
        CompletableFuture<BigFraction> cfb1 = CompletableFuture.supplyAsync(() -> BigFraction.reduce(b1));
        CompletableFuture<BigFraction> cfb2 = CompletableFuture.supplyAsync(() -> BigFraction.reduce(b2));
        cfb1.thenCombine(cfb2, BigFraction::add).thenAccept(System.out::println);

        //two stage or
        List<BigFraction> list = new ArrayList<>();
        CompletableFuture<List<BigFraction>> quickSort = CompletableFuture.supplyAsync(() -> quickSort(list));
        CompletableFuture<List<BigFraction>> mergeSort = CompletableFuture.supplyAsync(() -> mergeSort(list));
        quickSort.acceptEither(mergeSort, results -> results.forEach(bigFraction -> System.out.println(bigFraction.toMixedString())));
    }


    private static <U> U mergeSort(List<BigFraction> list) {
        return null;
    }

    private static <U> U quickSort(List<BigFraction> list) {
        return null;
    }
}
