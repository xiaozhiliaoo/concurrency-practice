package org;

public class AccountSync2 implements Runnable {

	//保证了同一对象
	static AccountSync2 instance = new AccountSync2();
	static int i=0;
	//实例方法
	public synchronized void increase(){
		i++;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int j=0;j<10000000;j++){
			increase();
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
