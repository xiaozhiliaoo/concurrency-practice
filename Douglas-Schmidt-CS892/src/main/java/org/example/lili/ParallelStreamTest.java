package org.example.lili;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * @author lili
 * @date 2020/4/20 20:11
 * @description 多个并行流之间会并行吗？
 * @notes 在Java8的foreach()中使用return/break/continue
 * parallel和parallelStream区别？统计时间，异常处理，
 */
public class ParallelStreamTest {
    public static void main(String[] args) throws InterruptedException {

        Thread r = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    list.add("list1:" + String.valueOf(i));
                }
                list.parallelStream().forEach(x -> System.out.println(x));
            }
        });

        r.start();


        Thread r2 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> list2 = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    list2.add("list2:" + String.valueOf(i));
                }
                list2.parallelStream().forEach(x -> System.out.println(x));
            }
        });

        r2.start();


        r.join();
        r2.join();

        List<String> list3 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list3.add("list3:" + String.valueOf(i));
        }

//        list3.parallelStream().


        System.out.println("main thread");
    }
}
