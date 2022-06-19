package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:10
 */
public class PartWithGuard {
    protected boolean cond = false;

    synchronized void await() throws InterruptedException {
        while (!cond)
            wait();
        // any other code
    }

    synchronized void signal(boolean c) {
        cond = c;
        notifyAll();
    }
}
