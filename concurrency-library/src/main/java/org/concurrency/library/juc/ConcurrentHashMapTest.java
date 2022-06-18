package org.concurrency.library.juc;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author lili
 * @date 2020/5/30 0:06
 * @description
 * @notes
 */
public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String, LongAdder> freqs = new ConcurrentHashMap();
        for (int i = 0; i < 10; i++) {
            freqs.computeIfAbsent(String.valueOf(i), k -> new LongAdder()).increment();
        }
        for (int i = 0; i < 20; i++) {
            freqs.computeIfAbsent(String.valueOf(i), k -> new LongAdder()).increment();
        }
        for (int i = 0; i < 25; i++) {
            freqs.computeIfAbsent(String.valueOf(i), k -> new LongAdder()).increment();
        }
        System.out.println(freqs);

        List<String> search = freqs.search(10, (k, v) -> {
            if (k.equals("2")) {
                return Lists.newArrayList(k);
            }
            return Lists.newArrayList(k);
        });
        System.out.println(search);

        String reduce = freqs.reduce(10, (k, v) -> k + v, (v1, v2) -> v1 + v2);
        System.out.println(reduce);

        LongAdder longAdder = freqs.reduceValues(10, (v1, v2) -> new LongAdder());
        System.out.println(longAdder);
    }
}
