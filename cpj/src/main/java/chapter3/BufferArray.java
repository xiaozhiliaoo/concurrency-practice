package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:12
 */
public class BufferArray {
    protected final Object[] array;      // the elements
    protected int putPtr = 0;             // circular indices
    protected int takePtr = 0;

    BufferArray(int n) {
        array = new Object[n];
    }

    synchronized void insert(Object x) {  // put mechanics
        array[putPtr] = x;
        putPtr = (putPtr + 1) % array.length;
    }

    synchronized Object extract() {       // take mechanics
        Object x = array[takePtr];
        array[takePtr] = null;
        takePtr = (takePtr + 1) % array.length;
        return x;
    }
}
