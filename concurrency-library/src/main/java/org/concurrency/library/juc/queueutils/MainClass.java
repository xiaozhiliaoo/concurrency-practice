package org.concurrency.library.juc.queueutils;

import java.util.concurrent.BlockingQueue;

/**
 * Created by lili on 2017/7/23
 */
public class MainClass {
    public static void main(String[] args) {
        BlockingQueue queue = new BlockingQueueFactory.ArrayBlockingQueueFactory().createQueue();

    }
}
