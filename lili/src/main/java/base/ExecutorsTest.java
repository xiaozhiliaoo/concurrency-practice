package base;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author lili
 * @date 2020/5/25 10:54
 * @description
 * @notes
 */
public class ExecutorsTest {

    public static void print() {
        System.out.println("do Print");
        printTwo();
    }

    private static void printTwo() {
        System.out.println("do Print Two");
        printThree();
    }

    private static void printThree() {
        System.out.println("do Print printThree");
    }

    public static void main(String[] args) throws Exception {
        Executors.callable(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println(i);
                    Thread.sleep(40);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("eeee");
        }).call();

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadFactory.newThread(()-> System.out.println("444")).start();

        ThreadGroup group = new ThreadGroup("thread-Group");
        Thread thread = new Thread(group, ()-> {
            print();
        },"myThread",1);
        thread.start();



    }


}
