package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:11
 */
public class Host {
    protected final PartWithGuard part = new PartWithGuard();

    synchronized void rely() throws InterruptedException {
        part.await();
    }

    synchronized void set(boolean c) {
        part.signal(c);
    }
}
