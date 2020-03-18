package net.jcip.examples;

/**
 * LeftRightDeadlock
 *
 * Simple lock-ordering deadlock
 *
 * @author Brian Goetz and Tim Peierls
 */
public class LeftRightDeadlock {

    private final Object left = new Object();
    private final Object right = new Object();

    public void leftRight() {
        synchronized (left) {
            synchronized (right) {
                doSomething();
            }
        }
    }

    public void rightLeft() {
        synchronized (right) {
            synchronized (left) {
                doSomethingElse();
            }
        }
    }

    void doSomething() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void doSomethingElse() {
    }

    public static void main(String[] args) throws InterruptedException {
        LeftRightDeadlock lock = new LeftRightDeadlock();
        new ThreadA(lock).start();
        new ThreadB(lock).start();
//        Thread.sleep(1000);
    }




}

class ThreadA extends Thread{

    private LeftRightDeadlock lock;

    public ThreadA(LeftRightDeadlock lock){
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.doSomething();
    }
}

class ThreadB extends Thread{

    private LeftRightDeadlock lock;

    public ThreadB(LeftRightDeadlock lock){
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.doSomethingElse();
    }
}
