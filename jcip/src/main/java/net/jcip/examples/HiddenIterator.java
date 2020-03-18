package net.jcip.examples;

import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * HiddenIterator
 * <p/>
 * Iteration hidden within string concatenation
 *
 * @author Brian Goetz and Tim Peierls
 */
public class HiddenIterator {
    @GuardedBy("this")
    private final Set<Integer> set = new HashSet<Integer>();

    public synchronized void add(Integer i) {
        set.add(i);
    }

    public synchronized void remove(Integer i) {
        set.remove(i);
    }

    public void addTenThings() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            add(r.nextInt());
        }
        System.out.println("DEBUG: added ten elements to " + set);
    }

    /*public static class Add implements Runnable{
        @Override
        public void run() {
            HiddenIterator i = new HiddenIterator();
            i.addTenThings();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Add add = new Add();
        Thread a = new Thread(add);
        Thread a2 = new Thread(add);
        a.start();
        a2.start();
//        a.join();
//        a2.join();
        System.out.println("Over....");
    }*/
}
