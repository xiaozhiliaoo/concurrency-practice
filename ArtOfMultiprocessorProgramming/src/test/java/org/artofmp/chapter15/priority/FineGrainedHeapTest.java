/*
 * FineGrainedHeapTest.java
 * JUnit based test
 *
 * Created on March 9, 2007, 11:45 PM
 */

package org.artofmp.chapter15.priority;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.*;

/**
 *
 * @author mph
 */
public class FineGrainedHeapTest extends TestCase {
  static final int RANGE = 32 * 1024;
  private final static int THREADS = 32;
  private final static int TEST_SIZE = RANGE;
  private final static int PER_THREAD = TEST_SIZE / THREADS;
  static Random random = new Random();
  Thread[] thread = new Thread[THREADS];
  FineGrainedHeap<Integer> instance;
  Map<Integer,Integer> log = new ConcurrentHashMap<Integer,Integer>();
  
  public FineGrainedHeapTest(String testName) {
    super(testName);
  }
  
  @Override
  protected void setUp() throws Exception {
  }
  
  protected void tearDown() throws Exception {
  }
  
  /**
   * Test of add method, of class priority.FineGrainedHeap.
   */
  public void testSequential() {
    System.out.println("testSequential");
    int priority, value;
    
    instance = new FineGrainedHeap<Integer>(TEST_SIZE);
    log.clear();
    for (int i = 0; i < TEST_SIZE; i++) {
      priority = random.nextInt(RANGE);
      log.put(i, priority);
      instance.add(i, priority);
    }
    instance.sanityCheck();
    
    int oldPriority = -1;
    for (int i = 0; i < TEST_SIZE; i++) {
      value = instance.removeMin();
      instance.sanityCheck();
      priority = log.get(value);
      if (oldPriority > priority) {
        fail("non-ascending priorities: " + oldPriority + ", " + priority);
      }
      oldPriority = priority;
    }
    System.out.println("OK.");
  }
  /**
   * Parallel adds, sequential removeMin
   * @throws Exception
   */
  public void testParallelAdd()  throws Exception {
    System.out.println("testParallelAdd");
    int key, value;
    instance = new FineGrainedHeap<Integer>(TEST_SIZE);
    
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new AddThread(i * PER_THREAD);
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
    instance.sanityCheck();
    int oldKey = -1;
    for (int i = 0; i < TEST_SIZE; i++) {
      value = instance.removeMin();
      key = log.get(value);
      if (oldKey > key) {
        fail("non-ascending priorities!");
      }
      oldKey = key;
    }
    System.out.println("OK.");
  }
  /**
   * Parallel adds, sequential removeMin
   * @throws Exception
   */
  public void testParallelBoth()  throws Exception {
    System.out.println("testParallelBoth");
    int key, value;
    instance = new FineGrainedHeap<Integer>(TEST_SIZE);
    
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new AddThread(i * PER_THREAD);
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
    instance.sanityCheck();
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new RemoveMinThread();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
    System.out.println("OK.");
  }
  class AddThread extends Thread {
    int base;
    AddThread(int i) {
      base = i;
    }
    public void run() {
      for (int i = 0; i < PER_THREAD; i++) {
        int x = base + i;
        log.put(x, x);
        instance.add(x, x);
      }
    }
  }
  class RemoveMinThread extends Thread {
    int last;
    RemoveMinThread() {
      last = Integer.MIN_VALUE;
    }
    public void run() {
      for (int i = 0; i < PER_THREAD; i++) {
        int x = instance.removeMin();
        if (x < last) {
          fail("non-ascending priorities: " + last + ", " + x);
        }
        last = x;
      }
    }
  }
}
