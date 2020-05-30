package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:38
 * @description
 * @notes
 */
public class TwoLockQueue {
    final static class Node {
        Object value;
        Node next = null;

        Node(Object x) {
            value = x;
        }
    }

    private Node head_ = new Node(null); // dummy hdr
    private Node last_ = head_;
    private Object lastLock_ = new Object();

    void put(Object x) {
        synchronized (lastLock_) {
            last_ = last_.next = new Node(x);
        }
    }

    synchronized Object poll() { // null if empty
        Object x = null;
        Node first = head_.next; // only contention pt
        if (first != null) {
            x = first.value;
            first.value = null;
            head_ = first; // old first becomes header
        }
        return x;
    }
}
