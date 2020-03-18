package org;

public class AccountSync implements Runnable {

	static AccountSync instance = new AccountSync();
	static int i=0;
	@Override
	public  void run() {


		//System.out.println(Thread.currentThread().getId());
		// TODO Auto-generated method stub
		for(int j=0; j<10000; j++) {
			//synchronized(instance){
			//System.out.println(Thread.currentThread().getName()+"--"+i++);
			//}
			// WHY???
			//System.out.println(i);

			i++;
		}
	}
	//没有synchronized会导致临界区的数据操作失败

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread t1 = new Thread(instance);
		Thread t2 = new Thread(instance);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(i);
	}
}
