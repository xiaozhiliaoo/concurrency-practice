package chapter2;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author lili
 * @date 2022/6/19 1:44
 */
public class ExpandableArrayWithIterator extends ExpandableArray {
    protected int version = 0;

    public ExpandableArrayWithIterator(int cap) {
        super(cap);
    }

    public synchronized void removeLast()
            throws NoSuchElementException {
        super.removeLast();
        ++version;               // advertise update
    }

    public synchronized void add(Object x) {
        super.add(x);
        ++version;
    }

    public synchronized Iterator iterator() {
        return new EAIterator();
    }

    protected class EAIterator implements Iterator {
        protected final int currentVersion;
        protected int currentIndex = 0;

        EAIterator() {
            currentVersion = version;
        }

        public Object next() {
            synchronized (ExpandableArrayWithIterator.this) {
                if (currentVersion != version)
                    throw new ConcurrentModificationException();
                else if (currentIndex == size)
                    throw new NoSuchElementException();
                else
                    return data[currentIndex++];
            }
        }

        public boolean hasNext() {
            synchronized (ExpandableArrayWithIterator.this) {
                return (currentIndex < size);
            }
        }

        public void remove() {
            // similar
        }
    }
}