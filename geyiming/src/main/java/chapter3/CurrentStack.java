package chapter3;

/**
 * Created by lili on 2017/4/25.
 */

import java.util.concurrent.atomic.AtomicReference;

/**
 * Tribier算法实现的栈  基于Treiber算法实现的无阻塞的Stack
 */
public class CurrentStack<E> {
    AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();
    public void push(E item){
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do{
            oldHead = head.get();
            newHead.next = oldHead;
        }while(!head.compareAndSet(oldHead, newHead));
    }

    static class Node<E>{
        final E item;
        Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }

}
