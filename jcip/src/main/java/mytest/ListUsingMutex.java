package mytest;

/**
 * @packgeName: mytest
 * @ClassName: ListUsingMutex
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/8-20:41
 * @version: 1.0
 * @since: JDK 1.8
 */
public class ListUsingMutex {
    static class Node {
        Object item;
        Node next;
        Mutex lock = new Mutex();

        Node(Object x, Node n) {
            item = x;
            next = n;
        }
    }

    protected Node head;

    protected synchronized Node getHead() {
        return head;
    }

    public synchronized void add(Object x) {
        if (x == null) {
            throw new IllegalArgumentException();
        }
        head = new Node(x, head);
    }

    boolean search(Object x) {
        Node p = getHead();
        if (p == null || x == null) {
            return false;
        }
        p.lock.lock();

        for (; ; ) {
            Node nextp = null;
            boolean found;
            try {
                found = x.equals(p.item);
                if (!found) {
                    nextp = p.next;
                    nextp.lock.lock();
                }
            } finally {
                p.lock.unlock();
            }
            if (found) {
                return true;
            } else if (nextp == null) {
                return false;
            } else {
                p = nextp;
            }
        }
    }

    public static void main(String[] args) {
        Node n4 = new Node(4,null);
        Node n3 = new Node(3,n4);
        Node n2 = new Node(2,n3);
        Node n1 = new Node(1,n2);
        Node head = new Node(null,n1);
        ListUsingMutex list = new ListUsingMutex();
        list.add(head);
        list.add(n1);
        list.add(n2);
        list.add(n3);
        list.add(n4);
        System.out.println(list.search(2));

    }
}
