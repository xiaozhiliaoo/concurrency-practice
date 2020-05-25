package net.jcip.examples;

import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer2
 * <p/>
 * Replacing HashMap with ConcurrentHashMap
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer2 <A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new ConcurrentHashMap<A, V>();
    private final Computable<A, V> c;

    public Memoizer2(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        Memoizer2<String, String> memoizer2 = new Memoizer2<>(new Computable<String, String>() {
            @Override
            public String compute(String arg) throws InterruptedException {
                Thread.sleep(4000);
                return "Compute It:"+ arg;
            }
        });

        String memory2 = memoizer2.compute("Memory2");
        System.out.println(memory2);
    }
}
