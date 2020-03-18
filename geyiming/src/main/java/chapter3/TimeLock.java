package chapter3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TimeLock implements Runnable{

	public static ReentrantLock lock = new ReentrantLock();

	public void run() {
		try {
			if(lock.tryLock(5,TimeUnit.SECONDS)){
				Thread.sleep(6000);
			}else{
				System.out.println("get lock failed");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
	}

	/**
	 *lili
	 *2017-4-9 下午12:14:29
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TimeLock tL = new TimeLock();
		Thread t1 = new Thread(tL);
		Thread t2 = new Thread(tL);
		t1.start();
		t2.start();
	}

}
