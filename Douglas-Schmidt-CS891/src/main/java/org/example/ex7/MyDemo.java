package org.example.ex7;

import java.util.function.Supplier;

/**
 * @author lili
 * @date 2020/4/21 0:18
 * @description
 * @notes
 */
class MyDemo2 implements Runnable {
    @Override
    public void run() {
        System.out.println("MyDemo2....");
    }
}

public class MyDemo {
    public static void main(String[] args) throws InterruptedException {
        Supplier<MyDemo> aNew = MyDemo::new;
        System.out.println(aNew.get());

        Runnable aNew1 = new MyDemo2();
        Thread thread = new Thread(aNew1);
        thread.start();
        thread.join();

        Runnable aNew2 = MyDemo2::new;
        Thread thread2 = new Thread(aNew2);
        thread2.start();
        thread2.join();

    }
}
