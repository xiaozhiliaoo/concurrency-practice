package chapter4.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by lili on 2017/4/30.
 */
public class HeavySocketClient {

    private static ExecutorService es = Executors.newCachedThreadPool();
    private static final int SLEEP_TIME = 1000*1000*1000;

    public static class EchoClient implements Runnable{

        @Override
        public void run() {
            Socket client = null;
            PrintWriter writer = null;
            BufferedReader reader = null;

            try {
                client = new Socket();
                client.connect(new InetSocketAddress("127.0.0.1",8080));
                writer = new PrintWriter(client.getOutputStream(), true);
                writer.print("h");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("e");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("l");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("o");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("!");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.print("!");
                LockSupport.parkNanos(SLEEP_TIME);
                writer.println();
                writer.flush();

                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from server:" + reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(writer!=null){writer.close();}
                    if(reader!=null){reader.close();}
                    if(client!=null){client.close();}
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        EchoClient ec = new EchoClient();
        for (int i = 0; i < 10; i++) {
            es.submit(ec);
        }
    }
}
