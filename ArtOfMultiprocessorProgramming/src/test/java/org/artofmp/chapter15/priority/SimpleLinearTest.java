/*
 * SimpleLinearTest.java
 * JUnit based test
 *
 * Created on March 9, 2007, 11:45 PM
 */

package org.artofmp.chapter15.priority;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import junit.framework.*;

/**
 *
 * @author mph
 */
public class SimpleLinearTest extends TestCase {
  static final int TEST_SIZE = 128;
  static Random random = new Random();
  static final int RANGE = TEST_SIZE;
  private final static int THREADS = 8;
  private final static int PER_THREAD = TEST_SIZE / THREADS;
  SimpleLinear<Integer> instance;
  Thread[] thread = new Thread[THREADS];
  Map<Integer,Integer> log = new ConcurrentHashMap<Integer,Integer>();
  
  public SimpleLinearTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  /**
   * Test of add method, of class priority.SimpleLinear.
   */
  public void testAdd() {
    System.out.println("add");
    Map<Integer,Integer> log = new HashMap<Integer,Integer>();
    int key, value;
    
    SimpleLinear<Integer> instance = new SimpleLinear<Integer>(RANGE);
    for (int i = 0; i < TEST_SIZE; i++) {
      key = random.nextInt(RANGE);
      value = random.nextInt();
      log.put(value, key);
      instance.add(value, key);
    }
    
    // validate
    int oldKey = -1;
    for (int i = 0; i < TEST_SIZE; i++) {
      value = instance.removeMin();
      key = log.get(value);
      if (oldKey > key) {
        fail("non-ascending keys!");
      }
      System.out.println("OK.");
    }
  }
  /**
   * Parallel adds, sequential removeMin
   * @throws Exception
   */
  public void testParallelAdd()  throws Exception {
    System.out.println("testParallelAdd");
    int value, priority;
    instance = new SimpleLinear<Integer>(RANGE);
    
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
    instance = new SimpleLinear<Integer>(RANGE);
    
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
