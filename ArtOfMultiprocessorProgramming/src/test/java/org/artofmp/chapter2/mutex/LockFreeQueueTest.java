/*
 * LockFreeQueueTest.java
 * JUnit based test
 *
 * Created on December 27, 2005, 11:15 PM
 */

package org.artofmp.chapter2.mutex;

import junit.framework.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Two-thread lock-free queue tester
 * @author Maurice Herlihy
 */
public class LockFreeQueueTest extends TestCase {
  private final static int THREADS = 2; // no more than 2
  private final static int TEST_SIZE = 512;
  private final static int PER_THREAD = TEST_SIZE / THREADS;
  int index;
  LockFreeQueue instance;
  boolean[] map = new boolean[TEST_SIZE];
  Thread[] thread = new Thread[THREADS];
  
  public LockFreeQueueTest(String testName) {
    super(testName);
    instance = new LockFreeQueue(TEST_SIZE);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(LockFreeQueueTest.class);
    return suite;
  }
  
  /**
   * Sequential calls.
   */
  public void testSequential() {
    System.out.println("sequential push and pop");
    
    for (int i = 0; i < TEST_SIZE; i++) {
      instance.enq(i);
    }
    for (int i = 0; i < TEST_SIZE; i++) {
      int j = (Integer)instance.deq();
      if (j != i) {
        fail("bad deq: " + j + " expected " + i);
      }
    }
  }
  /**
   * Parallel enLockFreeQueues, sequential deLockFreeQueues
   */
  public void testParallelEnq()  throws Exception {
    System.out.println("parallel enq");
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new EnqThread(i * PER_THREAD);
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
    for (int i = 0; i < TEST_SIZE; i++) {
      int j = (Integer)instance.deq();
      if (map[j]) {
        fail("duplicate pop: " + j);
      } else {
        map[j] = true;
      }
    }
  }
  /**
   * Sequential enLockFreeQueues, parallel deLockFreeQueues
   */
  public void testParallelDeq()  throws Exception {
    System.out.println("parallel deq");
    for (int i = 0; i < TEST_SIZE; i++) {
      map[i] = false;
    }
    for (int i = 0; i < TEST_SIZE; i++) {
      instance.enq(i);
    }
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new DeqThread();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
  }
  /**
   * Sequential enLockFreeQueues, parallel deLockFreeQueues
   */
  public void testParallelBoth() throws Exception {
    System.out.println("parallel both");
    Thread[] myThreads = new Thread[2 * THREADS];
    for (int i = 0; i < THREADS; i++) {
      myThreads[i] = new EnqThread(i * PER_THREAD);
      myThreads[i + THREADS] = new DeqThread();
    }
    for (int i = 0; i < 2 * THREADS; i ++) {
      myThreads[i].start();
    }
    for (int i = 0; i < 2 * THREADS; i ++) {
      myThreads[i].join();
    }
  }
  class EnqThread extends Thread {
    int value;
    EnqThread(int i) {
      value = i;
    }
    public void run() {
      for (int i = 0; i < PER_THREAD; i++) {
        instance.enq(value + i);
      }
    }
  }
  class DeqThread extends Thread {
    public void run() {
      for (int i = 0; i < PER_THREAD; i++) {
        int value = (Integer)instance.deq();
        if (map[value]) {
          fail("DeqThread: duplicate pop");
        }
        map[value] = true;
      }
    }
  }
  
}
