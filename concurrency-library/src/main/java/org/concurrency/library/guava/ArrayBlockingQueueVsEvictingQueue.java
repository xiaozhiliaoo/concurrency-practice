package org.concurrency.library.guava;

import com.google.common.collect.EvictingQueue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author lili
 * @date 2020/4/27 1:38
 * @description
 * @notes
 */
public class ArrayBlockingQueueVsEvictingQueue {
    public static void main(String[] args) {
        testEvictingQueue();
        testArrayBlockingQueue();
    }

    private static void testArrayBlockingQueue() {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(10);
        for (int i = 1; i <= 15; i++) {
            blockingQueue.add("hi" + i);
            System.out.println("ArrayBlockingQueue size: " + blockingQueue.size());
        }
    }

    private static void testEvictingQueue() {
        Queue<String> objects = EvictingQueue.create(10);
        for (int i = 1; i <= 15; i++) {
            objects.add("hi" + i);
            System.out.println("EvictingQueue size: " + objects.size());
        }

    }
}
