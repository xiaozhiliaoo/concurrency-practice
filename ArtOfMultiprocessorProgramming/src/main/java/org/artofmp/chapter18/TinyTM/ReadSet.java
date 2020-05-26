/*
 * ReadSet.java
 *
 * Created on May 30, 2007, 8:44 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */
package org.artofmp.chapter18.TinyTM;


import java.util.AbstractSet;

/**
 * ReadSet.java
 * Keep track of transactions that opened this object for READ.
 *
 * @author Maurice Herlihy
 */
public class ReadSet extends AbstractSet<Transaction> {

    /**
     * This value is public to facilitate unit testing.
     */
    public static final int INITIAL_SIZE = 64;
    /**
     * Number of allocated slots. Must reallocate if actual number of
     * transactions exceeds this size.
     */
    private int size;
    /**
     * Next free slot in array.
     */
    private int next;
    /**
     * Iterates over elements.
     */
    private Transaction elements[];

    /**
     * Create ReadSet of default size.
     */
    public ReadSet() {
        this(INITIAL_SIZE);
    }

    /**
     * Create ReadSet of indicated size.
     *
     * @param size Size of readSet to create.
     */
    public ReadSet(int size) {
        this.size = size;
        elements = new Transaction[size];
        next = 0;
    }

    /**
     * Initialize one object from another.
     *
     * @param aSet Initialize from this other object.
     */
    public void copyFrom(ReadSet aSet) {
        if (aSet.size > this.size) {
            elements = new Transaction[aSet.size];
            this.size = aSet.size;
        }
        System.arraycopy(aSet.elements, 0, this.elements, 0, aSet.next);
        this.next = aSet.next;
    }

    /**
     * Add a new transaction to the set.
     *
     * @param t Transaction to add.
     * @return Whether this transaction was already present.
     */
    @Override
    public boolean add(Transaction t) {
        // try to reuse slot
        for (int i = 0; i < next; i++) {
            if (elements[i].getStatus() != Transaction.Status.ACTIVE) {
                elements[i] = t;
                return true;
            } else if (elements[i] == t) {
                return true;
            }
        }
        // check for overflow
        if (next == size) {
            Transaction[] newElements = new Transaction[2 * size];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
            size = 2 * size;
        }
        elements[next++] = t;
        return true;
    }

    /**
     * remove transaction from the set.
     *
     * @param t Transaction to remove.
     * @return Whether this transaction was already present.
     */
    public boolean remove(Transaction t) {
        // try to reuse slot
        int i = 0;
        boolean present = false;
        while (i < next) {
            if (elements[i] == t) {
                elements[i] = elements[next--];
                present = true;
            } else {
                i++;
            }
        }
        return present;
    }

    /**
     * Discard all elements of this set.
     */
    public void clear() {
        next = 0;
    }

    /**
     * How many transactions in the set?
     *
     * @return Number of transactions in the set.
     */
    public int size() {
        return next;
    }

    /**
     * Iterate over transaction in the set.
     *
     * @return Iterator over transactions in the set.
     */
    public java.util.Iterator<Transaction> iterator() {
        return new Iterator();
    }

    /**
     * Inner class that implements iterator.
     */
    private class Iterator implements java.util.Iterator<Transaction> {
        /**
         * Iterator position.
         */
        int pos = 0;

        /**
         * Is there another transaction in the set?
         *
         * @return whether there are more active transactions
         */
        public boolean hasNext() {
            //      if (pos == next) {
            //        return false;
            //      }
            //      while (pos < next && !elements[pos].isActive()) {
            //        elements[pos] = elements[next-1]; // discard inactive transactions
            //        next--;
            //      }
            return pos < next;
        }

        /**
         * Get next item in the set.
         *
         * @return next transaction in the set.
         */
        public Transaction next() {
            return elements[pos++];
        }

        /**
         * Do not call this method.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
