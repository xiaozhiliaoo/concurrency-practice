package chapter2;

import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:47
 */
public class ThreadWithOutputStream extends Thread {
    private OutputStream output;

    ThreadWithOutputStream(Runnable r, OutputStream s) {
        super(r);
        output = s;
    }

    static ThreadWithOutputStream current()
            throws ClassCastException {
        return (ThreadWithOutputStream) (currentThread());
    }

    static OutputStream getOutput() {
        return current().output;
    }

    static void setOutput(OutputStream s) {
        current().output = s;
    }
}
