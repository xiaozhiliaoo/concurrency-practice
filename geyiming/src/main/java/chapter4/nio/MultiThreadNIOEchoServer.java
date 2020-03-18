package chapter4.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lili on 2017/4/30.
 */
public class MultiThreadNIOEchoServer {
    public static Map<Socket, Long> time_stat = new HashMap<>(10240);
    private Selector selector;
    private ExecutorService es = Executors.newCachedThreadPool();

    class EchoClient{

        private LinkedList<ByteBuffer> outq;

        public EchoClient() {
            outq = new LinkedList<ByteBuffer>();
        }

        public void enqueue(ByteBuffer byteBuffer){
            outq.add(byteBuffer);
        }

        public LinkedList<ByteBuffer> getOutputQueue(){
            return outq;
        }
    }

    class HandleMsg implements Runnable{

        SelectionKey sk;
        ByteBuffer bb;
        //复杂业务可以进行多线程

        public HandleMsg(SelectionKey sk, ByteBuffer bb) {
            this.sk = sk;
            this.bb = bb;
        }

        @Override
        public void run() {
            EchoClient client = (EchoClient) sk.attachment();
            client.enqueue(bb);
            //修改感兴趣事件，希望回写事件，
            sk.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            //强迫Select进行返回  否则傻在那里
            selector.wakeup();
        }
    }



    private void startServer() throws IOException {
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        InetSocketAddress isa = new InetSocketAddress(8080);
        ssc.socket().bind(isa);
        SelectionKey acceptKey = ssc.register(selector,SelectionKey.OP_ACCEPT);

        for(;;){
            //会阻塞等待，选择事件，准备好读了，写了，accept 极少数线程监控客户端  等待时间分离到少数线程
            selector.select();
            Set readyKeys = selector.selectedKeys(); //key----Selector和Channel关系
            Iterator i = readyKeys.iterator(); //准备就绪的客户端  Socket
            long e = 0;
            // 通过事件通知来进行业务逻辑处理
            while(i.hasNext()){
                SelectionKey sk = (SelectionKey) i.next();
                i.remove();
                if(sk.isAcceptable()){
                    doAccept(sk);
                }else if(sk.isValid() && sk.isReadable()){
                    //统计读开始到回写回去花了多久
                    if(!time_stat.containsKey(((SocketChannel)sk.channel()).socket())){
                        time_stat.put(((SocketChannel)sk.channel()).socket(),
                                System.currentTimeMillis());
                    }
                    doRead(sk);
                }else if(sk.isValid() && sk.isWritable()){
                    //写准备好了 就做写的事件
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
                    System.out.println("Spend: " + (e-b) + " ms");
                }
            }
        }
    }

    private void doWrite(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        EchoClient echoClient = new EchoClient();
        LinkedList<ByteBuffer> outq = echoClient.getOutputQueue();
        ByteBuffer bb = outq.getLast();

        try {
            int len = channel.write(bb);
            if(len == -1){
                disconnect(sk);
                return;
            }
            if(bb.remaining() == 0){
                outq.removeLast();
            }
        } catch (IOException e) {
            System.out.println("fail to write from client");
            e.printStackTrace();
            disconnect(sk);
        }

        //回写完毕对写事件没关系 对读感兴趣
        if(outq.size() == 0){
            sk.interestOps(SelectionKey.OP_READ);
        }
    }

    private void doRead(SelectionKey sk) {
        SocketChannel channel = (SocketChannel) sk.channel();
        ByteBuffer bb = ByteBuffer.allocate(8192);
        int len;
        try {
            len = channel.read(bb);
            if(len < 0){
                disconnect(sk);
                return;
            }
        } catch (IOException e) {
            System.out.println("fail to read from client");
            e.printStackTrace();
            disconnect(sk);
            return;
        }

        bb.flip();
        //信息交给单独线程进程处理
        es.submit(new HandleMsg(sk,bb));

    }

    private void disconnect(SelectionKey sk) {
        sk.cancel();
    }

    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;

        try {
            //拿到客户端socket  客户端channel
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);
            //注册感兴趣的事件，读，
            SelectionKey clientKey = clientChannel.register(selector,SelectionKey.OP_READ);
            EchoClient echoClient = new EchoClient();
            clientKey.attach(echoClient);
            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accept connection form " + clientAddress.getHostAddress());
        } catch (IOException e) {
            System.out.println("Fail to accept new client");
            e.printStackTrace();
        } finally {
        }
    }


    public static void main(String[] args) throws IOException {
        MultiThreadNIOEchoServer server = new MultiThreadNIOEchoServer();
        server.startServer();
    }
}
