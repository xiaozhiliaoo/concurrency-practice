package chapter3;

import java.util.concurrent.locks.ReentrantLock;

public class FairLock implements Runnable{
	// TODO Auto-generated method stub
	public static ReentrantLock fairLock = new ReentrantLock();
	@Override
	public void run(){
		while(true){
			try {
				fairLock.lock();
				System.out.println(Thread.currentThread().getName()+"  获得锁.....");
			}finally{
				fairLock.unlock();
			}
		}
	}

	/**
	 *lili
	 *2017-4-9 下午1:05:59
	 * @param args
	 */
	public static void main(String[] args) {
		FairLock flock = new FairLock();
		Thread t1 = new Thread(flock,"t1");
		Thread t2 = new Thread(flock,"t2");
		t1.start();
		t2.start();
	}

}
