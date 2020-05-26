/*
 * BalancerTest.java
 * JUnit based test
 *
 * Created on June 9, 2006, 9:32 PM
 */

package org.artofmp.chapter12.counting;

import junit.framework.*;

/**
 *
 * @author mph
 */
public class BalancerTest extends TestCase {
  static final int TEST_SIZE = 256;
  
  public BalancerTest(String testName) {
    super(testName);
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(BalancerTest.class);
    
    return suite;
  }

  public void testTraverse() throws Exception {
    Balancer instance = new Balancer();
    int[] map = new int[]{0,0};
    for (int i = 0; i < TEST_SIZE; i++) {
      map[instance.traverse(0)]++;
    }
    checkStep(map);
  }
  
  private void checkStep(int[] a) throws Exception {
    boolean step = false;
    for (int i = 1; i < a.length; i++) {
      if (a[i] != a[i-1]) {
        if (!step && a[i] == a[i-1]-1) {
          step = true;
        } else {
          System.out.println("Step property failed");
          System.out.println(a);
          throw new Exception();
        }
      }
    }
  }
  
}
