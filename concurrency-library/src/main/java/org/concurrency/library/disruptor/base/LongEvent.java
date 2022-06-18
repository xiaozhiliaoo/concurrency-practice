package org.concurrency.library.disruptor.base;

/**
 * Created by lili on 2017/7/3.
 */

/**
 * 真正生产的对象，实际的消费数据,RingBuffer里面存储的
 */
public class LongEvent {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
