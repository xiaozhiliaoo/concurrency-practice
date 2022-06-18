package chapter3;

import java.io.IOException;
import java.net.Socket;

/**
 * @author lili
 * @date 2022/6/19 2:04
 */
public class ClientUsingSocket {                       // Code sketch
    int portnumber = 1234;
    String server = "gee";

    // ...
    Socket retryUntilConnected() throws InterruptedException {
        // first delay is randomly chosen between 5 and 10secs
        long delayTime = 5000 + (long) (Math.random() * 5000);
        for (; ; ) {
            try {
                return new Socket(server, portnumber);
            } catch (IOException ex) {
                Thread.sleep(delayTime);
                delayTime = delayTime * 3 / 2 + 1; // increase 50%
            }
        }
    }
}