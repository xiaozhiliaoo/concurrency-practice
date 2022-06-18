package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:54
 */
public class LinkedQueue {
    protected Node head = new Node(null);
    protected Node last = head;

    protected final Object pollLock = new Object();
    protected final Object putLock = new Object();

    public void put(Object x) {
        Node node = new Node(x);
        synchronized (putLock) {     // insert at end of list
            synchronized (last) {
                last.next = node;        // extend list
                last = node;
            }
        }
    }

    public Object poll() {         // returns null if empty
        synchronized (pollLock) {
            synchronized (head) {
                Object x = null;
                Node first = head.next;  // get to first real node
                if (first != null) {
                    x = first.object;
                    first.object = null;   // forget old object
                    head = first;            // first becomes new head
                }
                return x;
            }
        }
    }

    static class Node {            // local node class for queue
        Object object;
        Node next = null;

        Node(Object x) {
            object = x;
        }
    }
}
