package chapter3;

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo {

	public static Object u = new Object();
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super.setName(name);
		}

		@Override
		public void run(){
			synchronized(u){
				System.out.println("in " + getName());
				LockSupport.park();
			}
		}
	}


	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		t1.start();
		Thread.sleep(10000);
		t2.start();
		//系统级的实现，native，重入锁本身是应用层实现
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
		t1.join();
		t2.join();
	}

}
