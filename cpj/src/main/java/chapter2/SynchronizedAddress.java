package chapter2;

import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:49
 */
public class SynchronizedAddress extends Address {
    // ...
    public synchronized String getStreet() {
        return super.getStreet();
    }

    public synchronized void setStreet(String s) {
        super.setStreet(s);
    }

    public synchronized void printLabel(OutputStream s) {
        super.printLabel(s);
    }
}
