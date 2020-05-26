package base;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author lili
 * @date 2020/5/26 2:36
 * @description
 * @notes
 */
public class StreamBreak {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        LongStream.range(0, Integer.MAX_VALUE / 100).parallel().forEach(x -> {
            if (atomicBoolean.get()) {
                System.out.println(x);
                if (x >= 100) {
                    atomicBoolean.set(false);
                }
            }
        });
        System.out.println(System.currentTimeMillis() - l);
    }
}
