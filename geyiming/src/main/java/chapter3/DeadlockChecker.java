package chapter3;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * 死锁检查
 * @author lili
 *
 */
public class DeadlockChecker {

//	ConcurrentHashMap<K, V>

	private static final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

	final static Runnable deadlockCheck = new Runnable() {
		@Override
		public void run() {
			while(true){
				long[] deadlockedThreadIds = mbean.findMonitorDeadlockedThreads();
				if(deadlockedThreadIds != null){
					ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreadIds);
					for(Thread t : Thread.getAllStackTraces().keySet()){
						for(int i=0; i < threadInfos.length; i++){
							if(t.getId() == threadInfos[i].getThreadId()){
								//如果是死锁，则会进行中断
								t.interrupt();
							}
						}
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
	};

	public static void check(){
		Thread t = new Thread(deadlockCheck);
		//守护线程  why？ 不应该直接让jvm退出  只是后台处理
		t.setDaemon(true);
		t.start();
	}

}
