package atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicintegerFieldUpdaterTest {

	public static class Candidate{
		int id;
		// 必须定义  int改成AtomicInteger改动大
		volatile int score;
	}

	//CAS操作
	public final static AtomicIntegerFieldUpdater<Candidate> scoreUpdater
			= AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");

	public static AtomicInteger allScore = new AtomicInteger(0);

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Candidate stu = new Candidate();
		Thread[] t = new Thread[100];
		for (int i = 0; i < 100; i++) {
			t[i] = new Thread(){
				public void run(){
					if(Math.random()>0.4){
						scoreUpdater.incrementAndGet(stu);
						allScore.incrementAndGet();
					}
				}
			};
			t[i].start();
		}

		for (int i = 0; i < 100; i++) {
			t[i].join();

		}

		System.out.println("score= " + stu.score);
		System.out.println("allScore= " + allScore);
	}

}
