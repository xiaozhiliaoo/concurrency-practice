package chapter4;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lili
 * @date 2022/6/19 10:26
 */
public class WebService implements Runnable {
    static final int PORT = 1040;   // just for demo
    Handler handler = new Handler();

    public void run() {
        try {
            ServerSocket socket = new ServerSocket(PORT);
            for (; ; ) {
                final Socket connection = socket.accept();
                new Thread(new Runnable() {
                    public void run() {
                        handler.process(connection);
                    }
                }).start();
            }
        } catch (Exception e) {
        } // die
    }

    public static void main(String[] args) {
        new Thread(new WebService()).start();
    }
}
