/*
 * WorkStealing.java
 *
 * Created on March 26, 2006, 6:56 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

import java.util.Random;

/**
 * Dummy work-stealing thread.
 * @author Maurice Herlihy
 */
public class WorkStealingThread {  
  DEQueue[] queue;
  Random random;  
  /** Creates a new instance of WorkStealing 
   * @param queue array of double ended queues
   */
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
        int victim = random.nextInt(queue.length);
        if (!queue[victim].isEmpty()) {
          task = queue[victim].popTop();
        }
      }
    }
  }
  
  static class DEQueue {
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
