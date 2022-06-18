package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:45
 */
public class Cell {                                    // Do not use
    private long value;

    synchronized long getValue() {
        return value;
    }

    synchronized void setValue(long v) {
        value = v;
    }

    synchronized void swapValue(Cell other) {
        long t = getValue();
        long v = other.getValue();
        setValue(v);
        other.setValue(t);
    }
}
