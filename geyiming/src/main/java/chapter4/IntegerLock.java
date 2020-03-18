package chapter4;

/**
 * Created by lili on 2017/4/28.
 */
public class IntegerLock {

    static Integer i = 0;
    public static class AddThread extends Thread{
        @Override
        public void run(){
            for (int j = 0; j < 100000; j++) {
                //Integer是不可变对象  两个线程可能对不同的i的引用做操作  每i++一次  创建的是不同的对象
                synchronized (i){
                    i++;
                    //System.out.println(i);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread t1 = new AddThread();
        AddThread t2 = new AddThread();
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(i);
    }
}
