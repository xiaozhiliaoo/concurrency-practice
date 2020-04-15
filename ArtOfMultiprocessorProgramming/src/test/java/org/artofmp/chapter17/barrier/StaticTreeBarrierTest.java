/*
 * StaticTreeBarrierTest.java
 * JUnit based test
 *
 * Created on December 29, 2005, 9:56 PM
 */

package org.artofmp.chapter17.barrier;

import junit.framework.*;

/**
 *
 * @author mph
 */
public class StaticTreeBarrierTest extends TestCase {
  static final int THREADS = 7;   // number threads to test
  static final int THREADS3 = 13;   // number threads to test
  static final int ROUNDS = 1;    // how many rounds to test
  
  public StaticTreeBarrierTest(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(StaticTreeBarrierTest.class);
    
    return suite;
  }
  
  /**
   * Test of await method, of class barrier.StaticTreeBarrier.
   */
  public void testBarrier() { 
    System.out.format("Testing %d threads, %d rounds, radix 2\n", THREADS, ROUNDS); 
    StaticTreeBarrier instance = new StaticTreeBarrier(THREADS, 2);
    Thread[] thread = new Thread[THREADS];
    int[] log = new int[THREADS];
    for (int i = 0; i < THREADS; i++)
      log[i] = 2005;
    for (int j = 0; j < THREADS; j++) {
      thread[j] = new TestThread(j, THREADS, ROUNDS, instance, log);
    }
    for (int j = 0; j < THREADS; j++) {
      thread[j].start();
    }
    try {
      for (int j = 0; j < THREADS; j++) {
        thread[j].join();
      }
    } catch (InterruptedException e) {
      fail("interrupted exception");
    }
    System.out.println("radix 2 done");
    
  }
  /**
   * Test of await method, of class barrier.StaticTreeBarrier.
   */
  public void testBarrier3() { 
    ThreadID.reset();
    System.out.format("Testing %d threads, %d rounds, radix 3\n", THREADS3, ROUNDS); 
    StaticTreeBarrier instance = new StaticTreeBarrier(THREADS3, 3);
    Thread[] thread = new Thread[THREADS3];
    int[] log = new int[THREADS3];
    for (int i = 0; i < THREADS3; i++)
      log[i] = 2005;
    for (int j = 0; j < THREADS3; j++) {
      thread[j] = new TestThread(j, THREADS3, ROUNDS, instance, log);
    }
    for (int j = 0; j < THREADS3; j++) {
      thread[j].start();
    }
    try {
      for (int j = 0; j < THREADS3; j++) {
        thread[j].join();
      }
    } catch (InterruptedException e) {
      fail("interrupted exception");
    }
    System.out.println("radix 3 done");
    
  }
  public class TestThread extends Thread {
    int threads;
    int rounds;
    Barrier bar;
    int[] log;
    int index;
    public TestThread(int index, int threads, int rounds, Barrier bar, int[] log) {
      this.threads = threads;
      this.rounds = rounds;
      this.bar = bar;
      this.log = log;
      this.index = index;
    }
    public void run() {
      ThreadID.set(index);
      int sense = 0;
      for (int round = 0; round < rounds; round++) {
        log[index] = round;
        bar.await();  // let writers finish
        for (int i = 0; i < threads; i++) {
          if (log[i] != round) {
            System.out.format("%d\tError expected %d found %d at %d\n", index, round, log[i], i);
          }
        }
        bar.await();  // let readers finish
        sense = 1 - sense;
      }
    }
  }
  
}
