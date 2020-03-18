package atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {

	public final static AtomicReference<String> atomicStr = new AtomicReference<String>("abc");


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			final int num = 1;
			new Thread(){
				public void run(){
					try {
						Thread.sleep(Math.abs((int)(Math.random()*100)));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//一个多想，如果希望多线程修改引用保证线程安全
					if(atomicStr.compareAndSet("abc", "def")){
						System.out.println("Thread:"+ Thread.currentThread().getId()+"change value successful");
					}else{
						System.out.println("Thread:"+Thread.currentThread().getId()+"falid");
					}
				}
			}.start();
		}
	}

}
