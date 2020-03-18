/**
 *
 */
package org;

/**
 * @author lili
 *
 */
/**
 * jps
 * 然后 jstack 线程号
 */

public class BadSuspend {

	public static Object u = new Object();
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");
	public static class ChangeObjectThread extends Thread{
		public ChangeObjectThread(String name){
			super.setName(name);
		}
		@Override
		public void run() {
			synchronized(u){
				System.out.println("get resource...... " + getName());
				// 挂起导致U没办法释放
				Thread.currentThread().suspend();
			}
		}
	}
	public static void main(String[] args) throws InterruptedException{
		// 红点还在  说明主函数没结束
		t1.start();
//		Thread.sleep(10000);
		t2.start();
		t1.resume();
		t2.resume();
		t1.join();  //等待线程结束.......
		t2.join();
	}

}
