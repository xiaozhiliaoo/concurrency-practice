package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:15
 */
public class TSBoolean {
    private boolean value = false;

    // set to true; return old value
    public synchronized boolean testAndSet() {
        boolean oldValue = value;
        value = true;
        return oldValue;
    }

    public synchronized void clear() {
        value = false;
    }

}
