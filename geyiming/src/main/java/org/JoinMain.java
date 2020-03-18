package org;

public class JoinMain {

	public volatile static int i = 0;
	public static class AddThread extends Thread{
		@Override
		public void run(){
			for(i=0;i<1000000;i++){

			}
		}
	}
	public static void main(String[] args) throws InterruptedException{
		// TODO Auto-generated method stub
		AddThread addThread = new AddThread();
		addThread.start();
		// 先于主线程结束
		addThread.join();
		System.out.println(i);
	}

}
