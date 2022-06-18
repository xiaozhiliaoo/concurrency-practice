package chapter3;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lili
 * @date 2022/6/19 2:08
 */
public class ReaderWithTimeout {               // Generic code sketch
    // ...
    void process(int b) {
    }

    void attemptRead(InputStream stream, long timeout) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            for (; ; ) {
                if (stream.available() > 0) {
                    int c = stream.read();
                    if (c != -1) process(c);
                    else break; // eof
                } else {
                    try {
                        Thread.sleep(100); // arbitrary back-off time
                    } catch (InterruptedException ie) {
                        /* ... quietly wrap up and return ... */
                    }
                    long now = System.currentTimeMillis();
                    if (now - startTime >= timeout) {
                        /* ... fail ...*/
                    }
                }
            }
        } catch (IOException ex) { /* ... fail ... */ }
    }
}
