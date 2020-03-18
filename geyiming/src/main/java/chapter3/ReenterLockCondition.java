package chapter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable {

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			lock.lock();
			condition.await();
			System.out.println("Thread is going on....");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}


	public static void main(String[] args) throws InterruptedException {
		ReenterLockCondition reenterLockCondition = new ReenterLockCondition();
		Thread t1 = new Thread(reenterLockCondition);
		t1.start();
		Thread.sleep(5000);
		//被唤醒的线程必须重新获得锁
		lock.lock();
		condition.signal();
		lock.unlock();
		System.out.println("over");
	}
}
