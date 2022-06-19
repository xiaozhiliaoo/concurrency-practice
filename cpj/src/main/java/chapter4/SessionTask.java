package chapter4;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author lili
 * @date 2022/6/19 10:34
 */
public class SessionTask implements Runnable {  // generic code sketch
    static final int BUFFSIZE = 1024;

    protected final Socket socket;
    protected final InputStream input;

    SessionTask(Socket s) throws IOException {
        socket = s;
        input = socket.getInputStream();
    }

    void processCommand(byte[] b, int n) {
    }

    void cleanup() {
    }

    public void run() {            // Normally run in a new thread
        byte[] commandBuffer = new byte[BUFFSIZE];
        try {
            for (; ; ) {
                int bytes = input.read(commandBuffer, 0, BUFFSIZE);
                if (bytes != BUFFSIZE) break;
                processCommand(commandBuffer, bytes);
            }
        } catch (IOException ex) {
            cleanup();
        } finally {
            try {
                input.close();
                socket.close();
            } catch (IOException ignore) {
            }
        }
    }
}
