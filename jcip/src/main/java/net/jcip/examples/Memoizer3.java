package net.jcip.examples;

import java.util.*;
import java.util.concurrent.*;

/**
 * Memoizer3
 * <p/>
 * Memoizing wrapper using FutureTask
 *
 * @author Brian Goetz and Tim Peierls
 */
public class Memoizer3<A, V> implements Computable<A, V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<A, Future<V>>();
    private final Computable<A, V> c;

    public Memoizer3(Computable<A, V> c) {
        this.c = c;
    }

    public V compute(final A arg) throws InterruptedException {
        Future<V> f = cache.get(arg);
        if (f == null) {
            Callable<V> eval = new Callable<V>() {
                public V call() throws InterruptedException {
                    return c.compute(arg);
                }
            };
            FutureTask<V> ft = new FutureTask<V>(eval);
            f = ft;
            cache.put(arg, ft);
            ft.run(); // call to c.compute happens here
        }

        try {
            return f.get();
        } catch (ExecutionException e) {
            throw LaunderThrowable.launderThrowable(e.getCause());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Memoizer3<String, String> Memoizer3 = new Memoizer3<>(new Computable<String, String>() {
            @Override
            public String compute(String arg) throws InterruptedException {
                Thread.sleep(4000);
                return "Compute It:"+ arg;
            }
        });

        String memory2 = Memoizer3.compute("Memory3");
        System.out.println(memory2);
    }
}