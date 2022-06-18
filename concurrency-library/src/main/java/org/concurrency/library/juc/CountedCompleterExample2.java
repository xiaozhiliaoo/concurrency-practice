package org.concurrency.library.juc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lili
 * @date 2020/5/30 0:53
 * @description
 * @notes
 */
public class CountedCompleterExample2 {
    public static void main(String[] args) {
        List<BigInteger> list = new ArrayList<>();
        for (int i = 3; i < 20; i++) {
            list.add(new BigInteger(Integer.toString(i)));
        }

        BigInteger sum = ForkJoinPool.commonPool().invoke(new FactorialTask(null,
                new AtomicReference<>(new BigInteger("0")),
                list));
        System.out.println("Sum of the factorials = " + sum);


    }

    private static class FactorialTask extends CountedCompleter<BigInteger> {

        private static int SEQUENTIAL_THRESHOLD = 5;
        private List<BigInteger> integerList;
        private AtomicReference<BigInteger> result;

        private FactorialTask (CountedCompleter<BigInteger> parent,
                               AtomicReference<BigInteger> result,
                               List<BigInteger> integerList) {
            super(parent);
            this.integerList = integerList;
            this.result = result;
        }

        @Override
        public BigInteger getRawResult () {
            return result.get();
        }


        @Override
        public void compute() {
            if(integerList.size() <= SEQUENTIAL_THRESHOLD) {
                sumFactorials();
                propagateCompletion();
            }else{
                int middle = integerList.size() / 2;
                List<BigInteger> newList = integerList.subList(middle, integerList.size());
                integerList = integerList.subList(0, middle);
                addToPendingCount(1);
                FactorialTask task = new FactorialTask(this, result, newList);
                task.fork();
                this.compute();
            }
        }

        private void addFactorialToResult (BigInteger factorial) {
            result.getAndAccumulate(factorial, (b1, b2) -> b1.add(b2));
        }

        private void sumFactorials () {
            for (BigInteger i : integerList) {
                addFactorialToResult(i);
            }
        }

    }

}
