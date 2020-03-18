package atomic;

class MyThread implements Runnable{

	private int ticket = 10;
	public void run(){
		for(int i=0;i<100;i++){
			if(ticket > 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName()+"卖票：ticket" + ticket--);
			}
			//System.out.println(Thread.currentThread().getName()+"卖票：ticket" + ticket--);
		}
	}
}


public class SyncTicket {

	/**
	 *lili
	 *2017-4-15 上午2:30:10
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyThread my = new MyThread();
		Thread t1 = new Thread(my);
		Thread t2 = new Thread(my);
		Thread t3 = new Thread(my);
		t1.start();
		t2.start();
		t3.start();
	}

}
