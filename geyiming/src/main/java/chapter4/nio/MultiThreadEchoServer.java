package chapter4.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MultiThreadEchoServer {

    private static ExecutorService ep = Executors.newCachedThreadPool();

    static class HandleMsg implements Runnable{

        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public  void run() {

                BufferedReader is = null;
                PrintWriter os = null;
            try {
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(),true);
                String inputLine = null;
                long b = System.currentTimeMillis();
                while((inputLine = is.readLine()) != null) {
                    System.out.println("客户端发来：" + inputLine);
                    //回写客户端
                    os.println("ServerBack:" + inputLine);
                }
                long e = System.currentTimeMillis();
                System.out.println("Spend:" + (e-b) +" ms");
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                try {
                    if(is!=null){is.close();}
                    if(os!=null){os.close();}
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        ServerSocket echoServer = null;
        Socket clientSocket = null;
        try {
            echoServer = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                clientSocket =  echoServer.accept();
                System.out.println("Server Start!!!");
                System.out.println(clientSocket.getRemoteSocketAddress()+" is connect!!!");
//                System.out.println(clientSocket.getInputStream());
                ep.submit(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
