package atomic;

public class Run1 implements Runnable {

	static int i = 0;
	static Run1 r1 = new Run1();
	public void run() {
		for(int k=0;k<10000;k++){
			//if(i<100){
			//System.out.println(Thread.currentThread().getName()+"------------"+i++);
			//}
			//System.out.println("hello");  //只要加了system 肯定30000 why

			i++;
			//System.out.println(i);
		}
		/*while(i > 0){
			System.out.println(Thread.currentThread().getName()+"---"+i--);
		}*/
	}

//	public Run1(String name){
//		super();
//		this.setName(name);
//	}

//	public Run1(){
//
//	}
	/**
	 *lili
	 *2017-4-15 上午1:11:20
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//Run1 r1 = new Run1("A");
		//Run1 r2 = new Run1("B");
		//r1.start();
		//r2.start();

		//三个线程在执行一个线程里面的任务
		Thread t1 = new Thread(r1,"A");
		Thread t2 = new Thread(r1, "B");
		Thread t3 = new Thread(r1, "C");
		t1.start();
		t2.start();
		t3.start();t1.join();t2.join();t3.join();
		//主线程main可能优于子线程结束
		System.out.println(i);
	}

}
