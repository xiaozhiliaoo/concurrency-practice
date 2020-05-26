/*
 * SequentialHeapTest.java
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
public class SequentialHeapTest extends TestCase {
  static final int RANGE = 32;
  private final static int THREADS = 8;
  private final static int TEST_SIZE = 512;
  private final static int PER_THREAD = TEST_SIZE / THREADS;
  static Random random = new Random();
  Thread[] thread = new Thread[THREADS];
  SequentialHeap<Integer> instance;
  Map<Integer,Integer> log = new ConcurrentHashMap<Integer,Integer>();
  
  public SequentialHeapTest(String testName) {
    super(testName);
  }
  
  protected void setUp() throws Exception {
  }
  
  protected void tearDown() throws Exception {
  }
  
  /**
   * Test of add method, of class priority.SequentialHeap.
   */
  public void testSequential() {
    System.out.println("testSequential");
    int priority, value;
    
    instance = new SequentialHeap<Integer>(TEST_SIZE);
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
}
