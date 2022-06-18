package chapter2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:48
 */
public class ServiceUsingThreadLocal {                   // Fragments
    static ThreadLocal output = new ThreadLocal();

    public void service() {
        try {
            final OutputStream s = new FileOutputStream("...");
            Runnable r = new Runnable() {
                public void run() {
                    output.set(s);
                    try {
                        doService();
                    } catch (IOException e) {
                    } finally {
                        try {
                            s.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            };
            new Thread(r).start();
        } catch (IOException e) {
        }
    }

    void doService() throws IOException {
        ((OutputStream) (output.get())).write(0);
        // ...
    }
}