package org.concurrency.library.disruptor.base;

import com.lmax.disruptor.EventHandler;

/**
 * Created by lili on 2017/7/3.
 */

/**
 * 可以理解为消费者  实际处理数据  也可以理解为观察者模型，业务逻辑
 */
public class LongEventHandler  implements EventHandler<LongEvent> {
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
        //具体处理数据的逻辑
        System.out.println(longEvent.getValue());
    }
}
