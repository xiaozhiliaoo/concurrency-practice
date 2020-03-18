package zhangzhenhua.chapter3.demo4.thread;

public class ThreadB extends Thread {
	private Count count;
	public ThreadB(Count count) {
		this.count=count;
	}
	public void run() {
		count.lockMethod();
	}
}
