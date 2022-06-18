package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:52
 */
public class LinkedCell {
    protected int value;
    protected final LinkedCell next;

    public LinkedCell(int v, LinkedCell t) {
        value = v;
        next = t;
    }

    public synchronized int value() {
        return value;
    }

    public synchronized void setValue(int v) {
        value = v;
    }

    public int sum() {               // add up all element values
        return (next == null) ? value() : value() + next.sum();
    }

    public boolean includes(int x) { // search for x
        return (value() == x) ? true :
                (next == null) ? false : next.includes(x);
    }
}
