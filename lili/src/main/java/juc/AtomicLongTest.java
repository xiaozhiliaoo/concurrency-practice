package juc;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lili
 * @date 2022/6/17 14:54
 */
public class AtomicLongTest {

    public static void main(String[] args) {
        AtomicLong al = new AtomicLong(1);
        System.out.println(al.compareAndSet(0, 1));
        System.out.println(al);
        System.out.println(al.compareAndSet(1, 0));
        System.out.println(al);
    }
}
