package org.concurrency.library.disruptor.generate2;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import org.concurrency.library.disruptor.generate1.Trade;

public class Handler5 implements EventHandler<Trade>,WorkHandler<Trade> {
	  
    @Override  
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {  
        this.onEvent(event);  
    }  
  
    @Override  
    public void onEvent(Trade event) throws Exception {  
    	System.out.println("handler5: get price : " + event.getPrice());
    	event.setPrice(event.getPrice() + 3.0);
    }  
}  