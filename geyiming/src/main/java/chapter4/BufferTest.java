package chapter4;

import java.nio.ByteBuffer;

/**
 * Created by lili on 2017/4/30.
 */
public class BufferTest {





    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(15);
        System.out.println(buffer.limit()+"----"+buffer.capacity()+"-----"+buffer.position());
        for (int i = 0; i < 10; i++) {
            buffer.put((byte)i);
        }
        System.out.println(buffer.limit()+"----"+buffer.capacity()+"-----"+buffer.position());
        buffer.flip();
        System.out.println(buffer.limit()+"----"+buffer.capacity()+"-----"+buffer.position());

        for (int i = 0; i < 5; i++) {
            System.out.println(buffer.get());
        }
        System.out.println("---------------------");
        System.out.println(buffer.limit()+"----"+buffer.capacity()+"-----"+buffer.position());
        buffer.flip();
        System.out.println(buffer.limit()+"----"+buffer.capacity()+"-----"+buffer.position());


    }
}
