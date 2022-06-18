package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:44
 */
public class EagerSingletonCounter {
    private final long initial;
    private long count;

    private EagerSingletonCounter() {
        initial = Math.abs(new java.util.Random().nextLong() / 2);
        count = initial;
    }

    private static final EagerSingletonCounter s =
            new EagerSingletonCounter();

    public static EagerSingletonCounter instance() {
        return s;
    }

    public synchronized long next() {
        return count++;
    }

    public synchronized void reset() {
        count = initial;
    }
}
