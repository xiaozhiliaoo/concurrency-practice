package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:52
 */
public class ServerWithAssignableHelper {
    private double state;
    private Helper helper = new Helper();

    synchronized void setHelper(Helper h) {
        helper = h;
    }

    public void service() {
        Helper h;
        synchronized (this) {
            state = 2.0f; // ...
            h = helper;
        }
        h.operation();
    }

    public synchronized void synchedService() { // see below
        service();
    }
}
