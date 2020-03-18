package chapter3;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 无等待的并发   真正的高并发
 * Synchronized,ReentrantLock 属于有等待的并发 阻塞并发  无论线程读写都会进行阻塞，低级并发
 *
 * @author lili
 *
 */
public class ReadWriteLockDemo{

	//测试用ReentrantLock模拟
	private static Lock lock = new ReentrantLock();
	//测试用读写锁来模拟
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock= readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();
	private int value;

	public Object handleRead(Lock lock) throws InterruptedException{
		try {
			lock.lock();
			Thread.sleep(1000);
			return value;
		} finally {
			lock.unlock();
		}
	}

	public void handleWrite(Lock lock, int index) throws InterruptedException{
		try {
			lock.lock();
			Thread.sleep(1000);
			value = index;
		} finally {
			lock.unlock();
		}
	}
	/**
	 *lili
	 *2017-4-9 下午1:44:39
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ReadWriteLockDemo rwDemo = new ReadWriteLockDemo();
		Runnable readRunnable = new Runnable(){
			@Override
			public void run(){
				try {
					rwDemo.handleRead(readLock);
					//rwDemo.handleRead(lock);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		Runnable writeRunnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					rwDemo.handleWrite(writeLock, new Random().nextInt());
					//rwDemo.handleWrite(lock, new Random().nextInt());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		//开启18个读线程
		for(int i=0;i<18;i++){
			new Thread(readRunnable).start();
		}

		//开启两个写线程
		for(int i=18;i<20;i++){
			new Thread(writeRunnable).start();
		}

		//使用读写锁使得读线程是并行的，而写线程是串行的


	}

}
