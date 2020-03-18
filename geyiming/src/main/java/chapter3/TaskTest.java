package chapter3;

public class TaskTest implements Runnable{


	/**
	 *lili
	 *2017-4-15 下午8:05:32
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TaskTest tt = new TaskTest();
		TaskTest tt2 = new TaskTest();

		for(int i=0;i<5;i++){
			new Thread(tt).start();
			new Thread(tt2).start();
		}

		/*TaskTest tt = new TaskTest();
		for(int i=0;i<10;i++){
			new Thread(tt).start();
//			new Thread(tt).start();
//			new Thread(tt).start();
//			new Thread(tt).start();

		}*/


		//三个线程处理十个任务   三个一样的
		/*TaskTest tt = new TaskTest();
		ExecutorService es = Executors.newFixedThreadPool(3);
		for(int i=0;i<10;i++) {
			es.execute(tt);
		}*/

	}

	//有没有synchronized
	@Override
	public synchronized void run() {
		long start = System.currentTimeMillis();

		try {
			//一个任务
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//把这句话输出十遍
		System.out.println("hello  "+ Thread.currentThread().getName());
		System.out.println(System.currentTimeMillis()-start);

		// TODO Auto-generated method stub

	}

}
