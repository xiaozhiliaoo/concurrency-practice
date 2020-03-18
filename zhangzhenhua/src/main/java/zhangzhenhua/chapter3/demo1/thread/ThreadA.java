package zhangzhenhua.chapter3.demo1.thread;

public class ThreadA extends Thread {
	private Count count;
	public ThreadA(Count count) {
		this.count=count;
	}
	public void run() {
		count.add();
	}
}
