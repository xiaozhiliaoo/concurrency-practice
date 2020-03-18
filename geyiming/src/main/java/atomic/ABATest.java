package atomic;

import java.util.concurrent.atomic.AtomicStampedReference;

public class ABATest {

	/**
	 *lili
	 *2017-4-15 上午11:51:45
	 * @param args
	 */
	//余额19元
	static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//三个充值线程，一个消费线程，只充值了一次
		for(int i=0;i<3;i++){
			final int timestamp = money.getStamp();
			new Thread() {
				public void run(){

					while(true){
						Integer m = money.getReference();
						if(m<20){
							if(money.compareAndSet(m, m+20, timestamp, timestamp+1)) {
								System.out.println("余额小于20，充值成功，余额："+money.getReference() +"元");
								break;
							}
						}else{
							System.out.println("余额大于20，无需充值");
							break;
						}
					}
				}

			}.start();
		}

		new Thread(){
			public void run(){
				//进行20次消费
				for (int i = 0; i < 20; i++) {
					while(true){
						int timestamp = money.getStamp();
						Integer m = money.getReference();
						//大于10块，就会消费掉10元
						if(m>10) {
							System.out.println("大于10元");
							if(money.compareAndSet(m, m-10, timestamp, timestamp+1)){
								System.out.println("成功消费了10元，余额" + money.getReference());
								break;
							}
						}else{
							System.out.println("没有足够余额");
							break;
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
		}.start();


	}

}
