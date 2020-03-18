package chapter3;

import java.util.concurrent.locks.ReentrantLock;

public class IntLock implements Runnable{


	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	int lock;
	public IntLock(int lock){
		this.lock = lock;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(lock == 1){
				//可中断的加锁   t1对lock1进行加锁
				lock1.lockInterruptibly();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				// TODO: handle exception
				lock2.lockInterruptibly();

			}else{
				lock2.lockInterruptibly();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				// TODO: handle exception
				lock1.lockInterruptibly();
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("中断后的异常....");
			e.printStackTrace();
		}finally{
			if(lock1.isHeldByCurrentThread()){
				lock1.unlock();
			}
			if(lock2.isHeldByCurrentThread()){
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getId()+"-----线程退出");
		}

	}

	/**
	 *lili
	 *2017-4-9 上午11:34:34
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		IntLock r1 = new IntLock(1);
		IntLock r2 = new IntLock(2);
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();t2.start();
		Thread.sleep(1000);
		t2.interrupt();//t2放弃对t1的申请，释放得到的lock2
//		DeadlockChecker


	}

}
