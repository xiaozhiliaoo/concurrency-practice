package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:09
 */
public class Stack {                                    // Fragments

    public synchronized boolean isEmpty() {
        return false; /* ... */
    }

    public synchronized void push(Object x) { /* ... */ }

    public synchronized Object pop() throws StackEmptyException {
        if (isEmpty())
            throw new StackEmptyException();
        else return null;
    }
}
