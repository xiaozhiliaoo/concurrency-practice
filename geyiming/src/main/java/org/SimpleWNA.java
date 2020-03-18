package org;

public class SimpleWNA {

	final static Object OBJECT = new Object();
	
	
	public static class T1 extends Thread{
		@Override
		public void run(){
			synchronized(OBJECT){
				System.out.println(System.currentTimeMillis()+"  T1 start");
				System.out.println(System.currentTimeMillis()+" T1 wait for object");
				try {
					OBJECT.wait();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(System.currentTimeMillis()+"  T1 end");
			}
		}
	}
	
	
	public static class T2 extends Thread{
		@Override
		public void run(){
			synchronized(OBJECT){
				System.out.println(System.currentTimeMillis()+" T2 start notify all thread");
				OBJECT.notifyAll();
				System.out.println(System.currentTimeMillis()+"  T2 end");
				try {
					Thread.sleep(2000);//�ͷ�monitor
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread t1 = new T1();
		Thread t1_1 = new T1();
		t1_1.start();
		t1.start();
		Thread.sleep(1000);
		Thread t2 = new T2();
		t2.start();

	}

}
