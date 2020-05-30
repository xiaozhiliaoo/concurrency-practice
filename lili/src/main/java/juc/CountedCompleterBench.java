package juc;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountedCompleter;

/**
 * @author lili
 * @date 2020/5/30 1:35
 * @description http://august.nagro.us/CountedCompleter.html
 * @notes
 */
public class CountedCompleterBench {

    static final int[][] arrays = new int[][]{
            new int[100], new int[10000], new int[10_000_000], new int[30_000_000], new int[50_000_000]
    };

    static {
        Random rand = new Random();
        for (int[] arr : arrays) {
            for (int i = 0; i < arr.length; ++i) arr[i] = rand.nextInt(200);
        }
    }

    public static long serial() {
        long res = 0;
        for (int i : arrays[0]) if (i < 150) {
            res += fnv(i) % 5;
        }
        return res;
    }

    public static long parallelStream() {
        return Arrays.stream(arrays[0])
                .parallel()
                .filter(i -> i < 150)
                .mapToLong(i -> fnv(i) % 5)
                .sum();
    }

    public static long customCC() {
        return new CustomCC(arrays[0]).invoke();
    }

    static final class CustomCC extends CountedCompleter<Long> {
        private static final int TARGET_LEAVES = Runtime.getRuntime().availableProcessors() << 2;
        // ie, every leaf should be process at least 10_000 elements
        private static final int MIN_SIZE_PER_LEAF = 10_000;

        private static int computeInitialPending(int size) {
            // if x = size / MIN_SIZE_PER_LEAF, and x < TARGET_LEAVES, create x leaves instead.
            // This ensures that every leaf does at least MIN_SIZE_PER_LEAF operations.
            int leaves = Math.min(TARGET_LEAVES, size / MIN_SIZE_PER_LEAF);
            // If the total # leaves = 0, then pending = 0 since we shouldn't fork.
            // Otherwise, pending = log2(leaves), as explained in the benchmark doc
            return leaves == 0
                    ? 0
                    : 31 - Integer.numberOfLeadingZeros(leaves);
        }

        final int[] arr;
        int pos, size;
        long res = 0;
        final CustomCC[] subs;

        public CustomCC(int[] arr) {
            this(null, arr, arr.length, 0, computeInitialPending(arr.length));
        }

        private CustomCC(CustomCC parent, int[] arr, int size, int pos, int pending) {
            super(parent, pending);
            this.arr = arr; this.size = size; this.pos = pos;
            subs = new CustomCC[pending];
        }

        @Override
        public void compute() {
            // subs.length === getPendingCount()
            for (int p = subs.length - 1; p >= 0; --p) {
                size >>>= 1;
                CustomCC sub = new CustomCC(this, arr, size, pos + size, p);
                subs[p] = sub;
                sub.fork();
            }

            // needed for c2 to remove array index checking... modified from java's Spliterator class
            int[] a; int i, hi;
            if ((a = arr).length >= (hi = pos + size) && (i = pos) >= 0 && i < hi) {
                do {
                    int e = a[i];
                    if (e < 150) res += fnv(e) % 5;
                } while (++i < hi);
            }

            tryComplete();
        }

        @Override
        public void onCompletion(CountedCompleter<?> caller) {
            for (CustomCC sub : subs) res += sub.res;
        }

        @Override
        public Long getRawResult() {
            return res;
        }
    }


    static final long FNV_64_PRIME = 0x100000001b3L;
    static final long FNV_64_OFFSET = 0xcbf29ce484222325L;

    static long fnv(int x) {
        long h = FNV_64_OFFSET;
        h ^= (x >>> 24);
        h *= FNV_64_PRIME;
        h ^= (x >>> 16) & 0xff;
        h *= FNV_64_PRIME;
        h ^= (x >>> 8) & 0xff;
        h *= FNV_64_PRIME;
        h ^= x & 0xff;
        h *= FNV_64_PRIME;
        return h;
    }


    public static void main(String[] args) {
        long x = System.currentTimeMillis();
        serial();
        System.out.println(System.currentTimeMillis()- x);

        long x2 = System.currentTimeMillis();
        parallelStream();
        System.out.println(System.currentTimeMillis()- x2);

        long x3 = System.currentTimeMillis();
        customCC();
        System.out.println(System.currentTimeMillis()- x3);

    }
}
