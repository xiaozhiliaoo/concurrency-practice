package chapter2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author lili
 * @date 2022/6/19 1:59
 */
public class CopyOnWriteArrayList {         // Incomplete
    protected Object[] array = new Object[0];

    protected synchronized Object[] getArray() {
        return array;
    }

    public synchronized void add(Object element) {
        int len = array.length;
        Object[] newArray = new Object[len + 1];
        System.arraycopy(array, 0, newArray, 0, len);
        newArray[len] = element;
        array = newArray;
    }

    public Iterator iterator() {
        return new Iterator() {
            protected final Object[] snapshot = getArray();
            protected int cursor = 0;

            public boolean hasNext() {
                return cursor < snapshot.length;
            }

            public Object next() {
                try {
                    return snapshot[cursor++];
                } catch (IndexOutOfBoundsException ex) {
                    throw new NoSuchElementException();
                }
            }

            public void remove() {
            }

        };
    }
}
