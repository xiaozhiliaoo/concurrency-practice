package atomic;

public class AtomicIntegerDemo2 {

	public static int i = 0;

	//内部类读取外部类
	public static class UnsafeAddThread implements Runnable{
		//public static int i = 0;
		@Override
		public void run(){
			for(int k=0;k<10000;k++){
				i++;
				//System.out.println(Thread.currentThread().getId()+"--"+ i);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread[] t = new Thread[10];
		for(int k=0;k<10;k++) {
			t[k] = new Thread(new UnsafeAddThread());
		}
		for(int k=0;k<10;k++) {
			t[k].start();
		}
		for(int k=0;k<10;k++) {
			t[k].join();
		}
		System.out.println(i);


		//Thread t = ;
		
		
		/*for(int i=0;i<10;i++) {
			new Thread(new UnsafeAddThread()).start();
		}
		
		System.out.println(i);*/
	}

}
