/*
 * BloomFilterTest.java
 * JUnit based test
 *
 * Created on January 13, 2007, 11:11 PM
 */

package org.artofmp.chapter18.TinyTM.locking;

import junit.framework.*;
import java.util.BitSet;

/**
 *
 * @author mph
 */
public class BloomFilterTest extends TestCase {
  
  public BloomFilterTest(String testName) {
    super(testName);
  }

  public void test() {
    BloomFilter filter = new BloomFilter(128);
    Object x0 = new Object();
    Object x1 = new Object();
    Object x2 = new Object();
    filter.add(x0);
    filter.add(x1);
    assertTrue("x0 not found", filter.contains(x0));
    assertTrue("x1 not found", filter.contains(x1));
    assertFalse("x2 not found", filter.contains(x2));
  }
  
}
