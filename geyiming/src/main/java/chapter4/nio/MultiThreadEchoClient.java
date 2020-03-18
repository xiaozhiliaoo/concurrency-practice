package chapter4.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by lili on 2017/4/30.
 */
public class MultiThreadEchoClient {

    public static void main(String[] args){
        Socket client = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        try {
            client = new Socket();
            client.connect(new InetSocketAddress("127.0.0.1",8080));
            // 输入：
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                System.out.println("请输入：");
                writer = new PrintWriter(client.getOutputStream(),true);
                String msg = bufferedReader.readLine();
                writer.println(msg);
                writer.flush();
                //服务器返回
                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from server:" + reader.readLine());
            }
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
