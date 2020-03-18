package chapter4;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * Created by lili on 2017/4/30.
 */
public class NIOCopyFile {

//    Buffer;
//    Channel;
    public static void nioCopyFile(String resoure,String destination) throws IOException {
        FileInputStream inputStream = new FileInputStream(resoure);
        FileOutputStream outputStream = new FileOutputStream(destination);
        FileChannel readChannel = inputStream.getChannel();
        FileChannel writeChannel = outputStream.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024*150);//150k  一次分配150K
        while(true) {
            //postition清0  limit设置为capatity位置
            buf.clear();
            int len = readChannel.read(buf);
            if(len == -1){
                break;
            }
            // limit设置为position   position清0  通常在读写转化时候用
            buf.flip();
            writeChannel.write(buf);// buf写到channel，
        }
        readChannel.close();
        writeChannel.close();
    }

    public static void main(String[] args) throws IOException {
        nioCopyFile("D:\\doc\\1.doc","D:\\doc\\2.doc");
    }
}
