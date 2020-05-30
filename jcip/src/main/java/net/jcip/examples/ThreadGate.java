package net.jcip.examples;

import net.jcip.annotations.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ThreadGate
 * <p/>
 * Recloseable gate using wait and notifyAll
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class ThreadGate {
    // CONDITION-PREDICATE: opened-since(n) (isOpen || generation>n)
    @GuardedBy("this")
    private boolean isOpen;
    @GuardedBy("this")
    private int generation;

    public synchronized void close() {
        isOpen = false;
    }

    public synchronized void open() {
        ++generation;
        isOpen = true;
        notifyAll();
    }

    // BLOCKS-UNTIL: opened-since(generation on entry)
    public synchronized void await() throws InterruptedException {
        int arrivalGeneration = generation;
        while (!isOpen && arrivalGeneration == generation)
            wait();
    }

    public static void main(String[] args) {
        ThreadGate gate = new ThreadGate();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " is wait...");
                    gate.await();
                    //gate.open();
                    System.out.println(Thread.currentThread().getName() + " is pass...");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //主线程open
        //gate.open();

        new Thread(gate::open).start();
    }
}
