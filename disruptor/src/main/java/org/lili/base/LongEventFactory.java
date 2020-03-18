package org.lili.base;

import com.lmax.disruptor.EventFactory;

/**
 * Created by lili on 2017/7/3.
 */
public class LongEventFactory implements EventFactory {
    public Object newInstance() {
        return new LongEvent();
    }
}
