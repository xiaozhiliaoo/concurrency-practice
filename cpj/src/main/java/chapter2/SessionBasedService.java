package chapter2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author lili
 * @date 2022/6/19 1:47
 */
public class SessionBasedService {                     // Fragments
    // ...
    public void service() {
        OutputStream output = null;
        try {
            output = new FileOutputStream("...");
            doService(output);
        } catch (IOException e) {
            handleIOFailure();
        } finally {
            try {
                if (output != null) output.close();
            } catch (IOException ignore) {
            } // ignore exception in close
        }
    }

    void handleIOFailure() {
    }

    void doService(OutputStream s) throws IOException {
        s.write(0);
        // ... possibly more handoffs ...
    }
}

