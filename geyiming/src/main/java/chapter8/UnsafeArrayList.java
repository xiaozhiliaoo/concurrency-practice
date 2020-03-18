package chapter8;


import java.util.ArrayList;

public class UnsafeArrayList {

    static ArrayList list = new ArrayList();
    public static class AddThread implements Runnable{


        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                list.add(new Object());
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new AddThread(),"t1");
        Thread t2 = new Thread(new AddThread(),"t2");

        t1.start();
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t3");

        t3.start();

    }
}
