/*
 * Mutex.java
 *
 * Created on August 26, 2006, 7:31 PM
 *
 * From "The Art of Multiprocessor Programming",
 * by Maurice Herlihy and Nir Shavit.
 * Copyright 2006 Elsevier Inc. All rights reserved.
 */

package org.artofmp.chapter8.monitor;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;

/**
 * Non-Reentrant mutual exclusion lock based on AbstractQueuedSynchronizer.
 * @author Maurice Herlihy
 */
class Mutex implements Lock {
  final static int FREE = 0;
  final static int BUSY = 1;
  
  // Inner helper class
  private static class LockSynch extends AbstractQueuedSynchronizer {
    // Report whether in locked state
    protected boolean isHeldExclusively() {
      return getState() == BUSY;
    }
    
    // Acquire the lock if state is FREE
    public boolean tryAcquire(int acquires) {
      return compareAndSetState(FREE, BUSY);
    }
    
    // Release the lock by setting state to FREE
    protected boolean tryRelease(int releases) {
      if (getState() == FREE)
        throw new IllegalMonitorStateException();
      setState(FREE);
      return true;
    }
    
    // Provide a Condition
    Condition newCondition() {
      return new ConditionObject();
    }
    
  }
  
  // The sync object does all the hard work. We just forward to it.
  private final LockSynch sync = new LockSynch();
  
  public void lock() {
    sync.acquire(0);
  }
  public boolean tryLock() {
    return sync.tryAcquire(0);
  }
  public void unlock() {
    sync.release(0);
  }
  public Condition newCondition() {
    return sync.newCondition();
  }
  public boolean isLocked() {
    return sync.isHeldExclusively();
  }
  public boolean hasQueuedThreads() {
    return sync.hasQueuedThreads();
  }
  public void lockInterruptibly() throws InterruptedException {
    sync.acquireInterruptibly(0);
  }
  public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
  }
}