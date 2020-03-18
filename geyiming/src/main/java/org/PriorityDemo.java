package org;

public class PriorityDemo {

	//加加前请求同一把锁  一起执行  就会遇到数据竞争
	public static class HightPriority extends Thread{
		static int count = 0;
		@Override
		public void run(){
			synchronized(PriorityDemo.class){
				while(true){
					count++;
					if(count > 10000000 ){
						System.out.println("HightPriority is complete" );
						break;
					}
				}
			}
			System.out.println(count);
		}
	}

	public static class LowPriority extends Thread{
		static int count = 0;
		@Override
		public void run(){
			synchronized(PriorityDemo.class){
				while(true){
					count++;
					if(count > 10000000 ){
						System.out.println("LowPriority is complete" );
						break;
					}
				}
			}
			System.out.println(count);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread high = new HightPriority();
		LowPriority low = new LowPriority();
		high.setPriority(Thread.MAX_PRIORITY);
		low.setPriority(Thread.MIN_PRIORITY);
		low.start();
		high.start();
	}

}
