package org;

public class VisibilityTest extends Thread{

	//以server模式启动  代码永远不会停止
	//可见性问题复杂，很多问题
	// 一个线程修改，另一个线程可见吗？
	private boolean stop;
	public void run(){
		int i =0;
		while(!stop){
			i++;
		}
		System.out.println("finish loop, i =" + i);
	}

	public void stopIt(){
		stop = true;
	}

	public boolean getStop(){
		return stop;
	}


	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		// -server模式会进行很多优化  启动会慢点
		VisibilityTest v = new VisibilityTest();
		v.start();
		Thread.sleep(1000);
		v.stopIt();
		Thread.sleep(2000);
		System.out.println("finish main");
		System.out.println(v.getStop());
	}

}
