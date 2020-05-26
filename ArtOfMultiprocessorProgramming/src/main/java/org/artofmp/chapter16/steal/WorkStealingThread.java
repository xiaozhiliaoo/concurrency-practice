/*
 * WorkStealingThread.java
 *
 * Created on April 7, 2006, 9:49 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter16.steal;

import java.util.Random;

/**
 * Typical work-stealing thread.
 * @author Maurice Herlihy
 */
public class WorkStealingThread {  
  DEQueue[] queue;
  int me;
  Random random;  
  public WorkStealingThread(DEQueue[] queue) {
    this.queue = queue;
    this.random = new Random();
  }
  public void run() {
    int me = ThreadID.get();
    Runnable task = queue[me].popBottom(); // pop first task
    while (true) {
      while (task != null) { // if there is a task
        task.run();          // execute it and then
        task = queue[me].popBottom(); // pop the next task
      }
      while (task == null) { // steal a task
        Thread.yield();
        int victim = random.nextInt() % queue.length;
        if (!queue[victim].isEmpty()) {
          task = queue[victim].popTop();
        }
      }
    }
  }
  
  class DEQueue {
    Runnable popBottom() {
      return null;
    }
    Runnable popTop() {
      return null;
    }
    boolean isEmpty() {
      return false;
    }
  }
  
}
