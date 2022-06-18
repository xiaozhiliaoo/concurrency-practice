package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:45
 */
public class StaticCounter {
    private static final long initial =
            Math.abs(new java.util.Random().nextLong() / 2);
    private static long count = initial;

    private StaticCounter() {
    } // disable instance construction

    public static synchronized long next() {
        return count++;
    }

    public static synchronized void reset() {
        count = initial;
    }
}
