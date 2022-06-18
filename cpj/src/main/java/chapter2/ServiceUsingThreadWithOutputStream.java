package chapter2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:48
 */
public class ServiceUsingThreadWithOutputStream {        // Fragments
    // ...
    public void service() throws IOException {
        OutputStream output = new FileOutputStream("...");
        Runnable r = new Runnable() {
            public void run() {
                try {
                    doService();
                } catch (IOException e) {
                }
            }
        };
        new ThreadWithOutputStream(r, output).start();
    }

    void doService() throws IOException {
        ThreadWithOutputStream.current().getOutput().write(0);
    }
}
