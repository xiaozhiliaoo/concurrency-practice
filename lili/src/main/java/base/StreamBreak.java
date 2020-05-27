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

    private volatile static boolean cancelled = false;


    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        LongStream.range(0, 100000).parallel().forEach(x -> {
            if (!cancelled) {
                if (x >= 99999) {
                    cancelled = true;
                    System.out.println("bigg " + x);
                } else {
                    System.out.println(x);
                }
            }
        });
    }
}
