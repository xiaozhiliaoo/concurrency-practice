package chapter3;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * CountDownLatch  一个线程等待其他
 * CyclicBarrier   多个线程互相等待  并且可以循环复用
 * @author lili
 *
 */
public class CyclicBarrierDemo {


	public static class Soldier implements Runnable{

		private String soldier;
		private final CyclicBarrier cyclicBarrier;
		Soldier(CyclicBarrier cyclicBarrier, String soldier) {
			super();
			this.soldier = soldier;
			this.cyclicBarrier = cyclicBarrier;
		}
		@Override
		public void run() {
			try {
				//集合
				cyclicBarrier.await();
				doWork();
				//开始任务
				cyclicBarrier.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		void doWork(){
			try {
				Thread.sleep(Math.abs(new Random().nextInt()%100000));
			} catch (InterruptedException e) {  //中断异常  长期等待把线程唤醒，防止卡死
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(soldier+"任务完成....");
		}
	};


	//结束触发   触发条件
	public static class BarrierRun implements Runnable{

		boolean flag;
		int N;

		public BarrierRun(boolean flag, int N) {
			this.flag = flag;
			this.N = N;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(flag) {
				System.out.println("司令：士兵" + N + "个，任务完成");
			} else {
				System.out.println("司令：士兵" + N + "个，集合完毕");
				flag = true;
			}
		}
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final int N = 10;
		Thread[] allSoldier = new Thread[N];
		boolean flag = false;
		CyclicBarrier cBarrier = new CyclicBarrier(N, new BarrierRun(flag, N));
		System.out.println("集合队伍");
		for(int i=0;i<N;i++){
			System.out.println("士兵"+i+"报道");
			allSoldier[i] = new Thread(new Soldier(cBarrier, "士兵"+i));
			allSoldier[i].start();
			if(i==5){
				allSoldier[0].interrupt();
			}
		}
	}


}










