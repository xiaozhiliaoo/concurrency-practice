package org.lili.base;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lili on 2017/7/3.
 */
public class LongEventMain {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        LongEventFactory factory = new LongEventFactory();
        int ringBufferSize = 1024 * 1024;

        Disruptor<LongEvent> disruptor =
                new Disruptor<LongEvent>(factory,ringBufferSize,executor, ProducerType.SINGLE,new YieldingWaitStrategy());

        disruptor.handleEventsWith(new LongEventHandler());

        disruptor.start();

        //发布事件  具体存放数据的容器   ringBufffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

//        LongEventProducer producer = new LongEventProducer(ringBuffer);

        LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer buffer = ByteBuffer.allocate(8);

        for (int i = 0; i < 100; i++) {
            buffer.putLong(0,i);
//            buffer.putInt(0,i);
            producer.onData(buffer);
        }

        disruptor.shutdown();
        executor.shutdown();




    }
}
