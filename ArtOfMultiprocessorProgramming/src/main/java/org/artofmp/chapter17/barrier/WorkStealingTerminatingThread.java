/*
 * WorkStealingTerminatingThread.java
 *
 * Created on March 26, 2006, 10:44 PM
 *
 * From "Multiprocessor Synchronization and Concurrent Data Structures",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter17.barrier;

import java.util.Random;

/**
 * Work-stealing thread with termination detection.
 * @author Maurice Herlihy
 */
public class WorkStealingTerminatingThread {
  DEQueue[] queue;
  TDBarrier tdBarrier;
  Random random;
  public WorkStealingTerminatingThread(int n) {
    queue = new DEQueue[n];
    tdBarrier = new SimpleTDBarrier(n);
    random = new Random();
    for (int i = 0; i < n; i++) {
      queue[i] = new DEQueue();
    }
  }
  public void run() {
    int me = ThreadID.get();
    tdBarrier.setActive(true);
    Runnable task = queue[me].popBottom(); // attempt to pop 1st item
    while (true) {
      while (task != null) { // if there is an item
        task.run();      // execute it and then
        task = queue[me].popBottom(); // pop the next item
      }
      tdBarrier.setActive(false); // no work
      while (task == null) { // steal an item
        int victim = random.nextInt(queue.length);
        if (!queue[victim].isEmpty()) {
          tdBarrier.setActive(true);  // tentatively active
          task = queue[victim].popTop();
          if (task == null) {
            tdBarrier.setActive(false);
          }
        }
        if (tdBarrier.isTerminated()) {
          return;
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