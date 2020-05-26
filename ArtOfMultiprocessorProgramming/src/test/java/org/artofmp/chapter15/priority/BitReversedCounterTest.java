/*
 * BitReversedCounterTest.java
 * JUnit based test
 *
 * Created on March 10, 2007, 7:11 PM
 */

package org.artofmp.chapter15.priority;

import junit.framework.*;

/**
 *
 * @author mph
 */
public class BitReversedCounterTest extends TestCase {
  
  public BitReversedCounterTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }

  /**
   * Test of increment method, of class priority.BitReversedCounter.
   */
  public void testIncrement() {
    System.out.println("increment");
    
    BitReversedCounter instance = new BitReversedCounter(0);
    for (int i = 0; i < 64; i++) {
      int j = instance.reverseIncrement();
      System.out.printf("inc:\t%d\t%d\n", i, j);
    }
    System.out.println("decrement");
    
    for (int i = 0; i < 32; i++) {
      System.out.println("dec:\t" + instance.reverseDecrement());
    }
    
  }

  
}
