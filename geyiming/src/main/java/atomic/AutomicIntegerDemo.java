package atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AutomicIntegerDemo {

	//定义十个线程，每个线程进行1万次相加
	private static AtomicInteger  i = new AtomicInteger();

	public static class AddThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(int k=0;k<10000;k++){
				i.incrementAndGet();
			}
		}
	}


	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread[] ts = new Thread[10];
		for(int k=0; k<10;k++){
			ts[k] = new Thread(new AddThread());
		}
		for(int k=0;k<10;k++){
			ts[k].start();
		}
		for(int k=0;k<10;k++){
			ts[k].join();
		}
		System.out.println(i);

	}

}
