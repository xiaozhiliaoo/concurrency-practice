package chapter3;

public class MyTest {


	public static class Task1 implements Runnable{

		static int i = 0;
		@Override
		public void run() {

			// TODO Auto-generated method stub
			for (int j = 0; j < 1000; j++) {
//				if(i>0) {
				i++;
				//System.out.println(Thread.currentThread().getName()+"___"+i);
//				}              
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Task1 t = new Task1();
		Task1 t2 = new Task1();
		Thread m = new Thread(t);
		m.start();
		Thread m1 = new Thread(t2);
		m1.start();
		m.join();
		m1.join();
		System.out.println(t.i);
		System.out.println(t2.i);

	}

}
