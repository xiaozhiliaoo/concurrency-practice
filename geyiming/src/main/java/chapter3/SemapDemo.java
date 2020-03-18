package chapter3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemapDemo implements Runnable {

	final Semaphore semp = new Semaphore(5);

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//信号量使用本质是对资源的分配
			semp.acquire();
			//模拟耗时操作  前5个线程可以立马拿到  done
//			Thread.sleep(5000);
			Thread.sleep((long) (Math.random() * 10000));
			System.out.println(Thread.currentThread().getName()+":done......");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			semp.release();
		}
	}

	/**
	 *lili
	 *2017-4-9 下午1:35:54
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		final SemapDemo demo = new SemapDemo();
		for(int i=0;i<20;i++){
			//执行线程
			executorService.submit(demo);
		}
	}
}
