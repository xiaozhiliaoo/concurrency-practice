package chapter4.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

/**
 * Created by lili on 2017/4/30.
 */
public class MultiThreadNIOEchoClient {
    private Selector selector;
    public void init(String ip, int port) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        this.selector = SelectorProvider.provider().openSelector();
        channel.connect(new InetSocketAddress(ip,port));
        channel.register(selector, SelectionKey.OP_CONNECT);
    }

    public void working() throws IOException{
        while(true){
            if(!selector.isOpen()){
                break;
            }
            selector.select();
            Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey  = iterator.next();
                iterator.remove();
                if(selectionKey.isConnectable()){
                    connect(selectionKey);
                }else if(selectionKey.isReadable()){
                    read(selectionKey);
                }
            }
        }
    }

    private void read(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        channel.read(byteBuffer);
        byte[] data = byteBuffer.array();
        String msg = new String(data).trim();
        System.out.println("客户端接受消息为：" + msg);
    }

    private void connect(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        if(channel.isConnectionPending()){
            channel.finishConnect();
        }
        channel.configureBlocking(false);
        channel.write(ByteBuffer.wrap(new String("hello server").getBytes()));
        channel.register(this.selector, SelectionKey.OP_READ);
        channel.close();
        selectionKey.selector().close();
    }
}
