package net.jcip.examples;

import net.jcip.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * UnsafeSequence
 *
 * @author Brian Goetz and Tim Peierls
 */

@NotThreadSafe
public class UnsafeSequence {
    private int value;

    /**
     * Returns a unique value.
     */
    public int getNext() {
        return value++;
    }

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();

    }
}
