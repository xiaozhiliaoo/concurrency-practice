package org;

public class SimpleWN {

	final static Object OBJECT = new Object();
	public static class T1 extends Thread{
		@Override
		public void run(){
			synchronized(OBJECT){
				System.out.println(System.currentTimeMillis()+"  T1 start");
				System.out.println(System.currentTimeMillis()+" T1 wait for object");
				try {
					OBJECT.wait();
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
				System.out.println(System.currentTimeMillis()+" T2 start notify one thread");
				OBJECT.notify();
				System.out.println(System.currentTimeMillis()+"  T2 end");
				try {
					System.out.println("等待5s后唤醒线程1结束......");
					Thread.sleep(5000);//释放monitor
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
		//Thread.sleep(1000);
		Thread t2 = new T2();
		t1.start();
		t2.start();
		//T2 end 2s后  才有了T1 end  T1往下走 必须拿到Object Monitor

	}

}
