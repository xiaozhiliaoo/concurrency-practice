package chapter3;

import java.util.concurrent.locks.ReentrantLock;

public class ReenterLock implements Runnable{

	public static ReentrantLock reentrantLock = new ReentrantLock();
	private static int i = 0;

	@Override
	public void run(){
		for(int j=0; j<1000000000;j++){
			//拿了一把锁的两个许可
			reentrantLock.lock();
			try{
				i++;
			}finally{
				reentrantLock.unlock();
				//jps  jstack pid
			}
		}
	}


	public static void main(String[] args) throws InterruptedException {
		Thread r1 = new Thread(new ReenterLock());
		Thread r2 = new Thread(new ReenterLock());
		r1.start();
		r2.start();
		r1.join();
		r2.join();
		System.out.println(i);

	}

}
