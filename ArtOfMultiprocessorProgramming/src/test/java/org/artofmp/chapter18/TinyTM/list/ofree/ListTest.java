/*
 * NodeTest.java
 * JUnit based test
 *
 * Created on January 14, 2007, 9:04 PM
 *
 * This work is licensed under a Creative Commons Attribution-Share Alike 3.0 United States License.
 * http://i.creativecommons.org/l/by-sa/3.0/us/88x31.png
 */

package org.artofmp.chapter18.TinyTM.list.ofree;
import junit.framework.TestCase;
import org.artofmp.chapter18.TinyTM.TThread;
import org.artofmp.chapter18.TinyTM.Transaction;
import org.artofmp.chapter18.TinyTM.list.INode;
import org.artofmp.chapter18.TinyTM.locking.OnAbort;
import org.artofmp.chapter18.TinyTM.locking.OnCommit;
import org.artofmp.chapter18.TinyTM.locking.OnStart;
import org.artofmp.chapter18.TinyTM.locking.OnValidate;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author mph
 */
public class ListTest extends TestCase {
  private final static int THREADS = 32;
  private final static int INITIAL_SIZE = 1024;
  private final static int MILLIS = 6000;
  AtomicInteger missed;
  AtomicInteger insert;
  AtomicInteger remove;
  Random random;
  int value;
  List<Integer> list;
  
  /**
   *
   * @param testName
   */
  public ListTest(String testName) {
    super(testName);
    insert = new AtomicInteger(0);
    remove = new AtomicInteger(0);
    missed = new AtomicInteger(0);
    random = new Random(this.hashCode());
    random.setSeed(System.currentTimeMillis()); // comment out for determinstic
  }
  
  void reset() throws InstantiationException, IllegalAccessException {
    insert.set(0);
    remove.set(0);
    missed.set(0);
    Transaction.setLocal(Transaction.COMMITTED);
    INode<Integer> next = new TNode<Integer>(Integer.MAX_VALUE);
    TThread.commits.set(0);
    TThread.aborts.set(0);
    list = new List<Integer>();
  }
  
    public void testParallel() throws Exception {
      reset();
      System.out.printf("TestParallel(%d)\n", THREADS);
      int size = 0;
      while (size < INITIAL_SIZE) {
        if (list.add(random.nextInt()))
          size++;
      }
      Thread[] threads = new Thread[THREADS];
      for (int i = 0; i < threads.length; i++) {
        threads[i] = new TestThread(i);
      }
      for (int i = 0; i < threads.length; i++) {
        threads[i].start();
      }
      Thread.sleep(MILLIS);
      for (int i = 0; i < threads.length; i++) {
        threads[i].interrupt();
      }
      for (int i = 0; i < threads.length; i++) {
        threads[i].join();
      }
      sanityCheck();
    }
  
  public void testSequential() throws Exception {
    reset();
    System.out.println("TestSequential");
    int size = 0;
    while (size < INITIAL_SIZE) {
      if (list.add(random.nextInt()))
        size++;
    }
    Thread thread = new TestThread(0);
    thread.start();
    Thread.sleep(MILLIS);
    thread.interrupt();
    thread.join();
    sanityCheck();
  }
  
  private class TestThread extends Thread {
    int index;
    public TestThread(int myID) {
      index = myID;
    }
    public void run() {
      try {
        boolean toggle = true;
        while (!Thread.currentThread().isInterrupted()) {
          value = random.nextInt(1000);
          boolean result = true;
          if (toggle) {        // insert on even turns
            final int arg = value;
            try {
              result = TThread.doIt(new Callable<Boolean>() {
                public Boolean call() {
                  return list.add(value);
                }
              });
            } catch (InterruptedException ex) {
              Transaction.getLocal().abort();
              throw ex;
            }
            if (result) {
              insert.getAndIncrement();
            } else {
              missed.getAndIncrement();
            }
          } else {              // remove on odd turns
            result = TThread.doIt(new Callable<Boolean>() {
              public Boolean call() throws InstantiationException, IllegalAccessException {
                return list.remove(value);
              }
            });
            if (result) {
              remove.getAndIncrement();
            } else {
              missed.getAndIncrement();
            }
          }
          toggle = !toggle;
        }
      } catch (InterruptedException e) {
        return;
      } catch (Exception e) {
        e.printStackTrace();
        return;
      }
      return;
    }
  }
  public void testIterator() throws Exception {
    int limit = 100;
    reset();
    for (int i = 0; i < limit; i++) {
      list.add(i);
    }
    int expected = 0;
    for (int v : list) {
      assertEquals("Iterator wrong", v, expected);
      expected++;
    }
    assertEquals("Iterator gave up: ", expected, limit);
  }
  
  public void sanityCheck() {
    long expected = insert.get() - remove.get() + INITIAL_SIZE;
    int length = 0;
    
    System.out.printf("inserts: %d, removes: %d, missed: %d, delta: %d\n",
        insert.get(), remove.get(), missed.get(), insert.get() - remove.get());
    System.out.printf("commits: %d, aborts: %d\n",
        TThread.commits.get(), TThread.aborts.get());
    int prevValue = Integer.MIN_VALUE;
    boolean firstTime = true;
    for (Integer value : list) {
      length++;
      if (value < prevValue) {
        fail("List not sorted");
      }
      if (value == prevValue) {
        fail("List has duplicate value:" + value);
      }
    }
    System.out.printf("length = %d (expected %d)\n", length, expected);
    System.out.printf("Commits: %d (expected %d)\n",
        TThread.commits.get(),
        insert.get() + remove.get() + missed.get());
    System.out.println("List OK");
    System.out.println("length = " + length);
    assertEquals("Wrong number of commits",
        insert.get() + remove.get() + missed.get(),
        TThread.commits.get());
    assertEquals("Bad size,", expected, length);
  }
}
