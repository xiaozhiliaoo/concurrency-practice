package chapter3;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class CompareConcurrentHashMap {

	private static ConcurrentHashMap<String, Integer>map = new ConcurrentHashMap(40000);

	public static void testPut(int index, int num){
		for(int i=index;i<(num+index);i++){
			map.put(String.valueOf(i),i);
		}
	}

	public static void testGet() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 400000; i++){
			map.get(String.valueOf(i));
		}
		long end = System.currentTimeMillis();
		System.out.println("get: it costs " + (end - start) + " ms");
	}
	/**
	 *lili
	 *2017-4-9 下午5:12:31
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		final CountDownLatch  cdl = new CountDownLatch(4);
		for(int i=0;i<4;i++){
			final int finalI = i;
			new Thread(new Runnable() {
				public void run() {
					CompareConcurrentHashMap.testPut(100000 * finalI, 100000);

				}
			}).start();
		}



	}

}
