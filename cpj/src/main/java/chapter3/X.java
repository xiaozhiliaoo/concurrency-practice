package chapter3;

/**
 * @author lili
 * @date 2022/6/19 2:11
 */
public class X {
    synchronized void w() throws InterruptedException {
        before();
        wait();
        after();
    }

    synchronized void n() {
        notifyAll();
    }

    void before() {
    }

    void after() {
    }
}
