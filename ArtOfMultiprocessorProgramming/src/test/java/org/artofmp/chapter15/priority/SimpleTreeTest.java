/*
 * SimpleTreeTest.java
 * JUnit based test
 *
 * Created on March 9, 2007, 11:45 PM
 */

package org.artofmp.chapter15.priority;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import junit.framework.*;

/**
 *
 * @author mph
 */
public class SimpleTreeTest extends TestCase {
  static final int LOG_RANGE = 6;
  static final int TEST_SIZE = 64;
  static Random random = new Random();
  private final static int THREADS = 8;
  private final static int PER_THREAD = TEST_SIZE / THREADS;
  SimpleTree<Integer> instance;
  Thread[] thread = new Thread[THREADS];
  
  public SimpleTreeTest(String testName) {
    super(testName);
  }
  
  protected void setUp() throws Exception {
  }
  
  protected void tearDown() throws Exception {
  }
  
  /**
   * Test of add method, of class priority.SimpleTree.
   */
  public void testAdd() {
    System.out.println("sequential test");
    Map<Integer,Integer> log = new HashMap<Integer,Integer>();
    int priority, value;
    
    SimpleTree<Integer> instance = new SimpleTree<Integer>(LOG_RANGE);
    int range = (1 << LOG_RANGE);
    for (int i = 0; i < TEST_SIZE; i++) {
      priority = random.nextInt(range);
      value = random.nextInt();
      log.put(value, priority);
      instance.add(value, priority);
    }
    
    // validate
    int oldKey = -1;
    for (int i = 0; i < TEST_SIZE; i++) {
      value = instance.removeMin();
            priority = log.get(value);
      if (oldKey > priority) {
        fail("non-ascending keys!");
      }
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
    instance = new SimpleTree<Integer>(LOG_RANGE);
    
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new AddThread(i * PER_THREAD);
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
    int oldValue = -1;
    for (int i = 0; i < TEST_SIZE; i++) {
      value = instance.removeMin();
      if (oldValue > value) {
        fail("non-ascending keys!");
      }
      oldValue = value;
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
    instance = new SimpleTree<Integer>(LOG_RANGE);
    
    for (int i = 0; i < THREADS; i++) {
      thread[i] = new AddThread(i * PER_THREAD);
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].start();
    }
    for (int i = 0; i < THREADS; i ++) {
      thread[i].join();
    }
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
