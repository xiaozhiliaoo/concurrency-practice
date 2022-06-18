package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:54
 */
public class SynchronizedInt {
    private int value;

    public SynchronizedInt(int v) {
        value = v;
    }

    public synchronized int get() {
        return value;
    }

    public synchronized int set(int v) { // returns previous value
        int oldValue = value;
        value = v;
        return oldValue;
    }

    public synchronized int increment() {
        return ++value;
    }

    // and so on

}