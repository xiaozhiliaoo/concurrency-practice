package chapter3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo implements Runnable {

	//十个检查任务
	static final CountDownLatch end = new CountDownLatch(10);

	static final CountDownLatchDemo demo = new CountDownLatchDemo();
	@Override
	public  void run() {
		try {
			//模拟每个线程检查任务   随机时间  准备任务线程通知主线程完成了
			Thread.sleep(5000);
			System.out.println(Thread.currentThread().getName()+":complete!!!");
			end.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		for(int i=0;i<10;i++) {
			executorService.submit(demo);
		}

		//等待检查
		end.await();
		//检查完成 发射火箭  通知主线程我做完了
		System.out.println("fire");
		executorService.shutdown();

	}

}
