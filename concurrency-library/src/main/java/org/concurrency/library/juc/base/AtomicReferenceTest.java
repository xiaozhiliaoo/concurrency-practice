package org.concurrency.library.juc.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author lili
 * @date 2020/4/30 13:45
 * @description
 * @notes
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {
        AtomicReference<BigDecimal> one = new AtomicReference<BigDecimal>(new BigDecimal(1));
        System.out.println(one.getAndSet(new BigDecimal(8)));//1
        System.out.println(one);//8
        System.out.println(one.get().add(new BigDecimal(8)));//16

        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        AtomicReference<BigDecimal> two = new AtomicReference<BigDecimal>(new BigDecimal(8));
        for (String s : list) {
            two.updateAndGet(x->x.add(new BigDecimal(2)));
        }
        System.out.println("two is:" + two.get());


        BigDecimal decimal = new BigDecimal(8);

        for (String s : list) {
            decimal = decimal.add(new BigDecimal(2));
        }
        System.out.println("decimal:"+decimal.toPlainString());

//        list.forEach(x-> {
//            two.getAndSet(new BigDecimal(8));
//        });
//        list.parallelStream().forEach(x -> {
//            two.getAndSet(new BigDecimal(8));
//        });
        System.out.println(two.get());

    }
}
