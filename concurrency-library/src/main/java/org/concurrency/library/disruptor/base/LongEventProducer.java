package org.concurrency.library.disruptor.base;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Created by lili on 2017/7/3.
 */
public class LongEventProducer {

    private RingBuffer<LongEvent> ringBuffer;


    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb){
            long sequence = ringBuffer.next();
        try {
            //环形结构存的是LongEvent
            LongEvent event = ringBuffer.get(sequence);
            event.setValue(bb.getLong(0));
        } finally {
            // 消息发布
            ringBuffer.publish(sequence);
        }
    }
}
