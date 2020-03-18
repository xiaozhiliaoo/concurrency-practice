package mytest;

import java.util.HashMap;
import java.util.UUID;

/**
 * @packgeName: mytest
 * @ClassName: UnsafeHashMap
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/8-19:12
 * @version: 1.0
 * @since: JDK 1.8
 */




public class UnsafeHashMap {

    public static void main(String[] args) throws InterruptedException {

        final HashMap<String,String> map = new HashMap<>();
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
                for (int i = 0; i < 100000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(),"");
                        }
                    }).start();
                }
            }
//        });
//        t.start();
//        t.join();

    }

