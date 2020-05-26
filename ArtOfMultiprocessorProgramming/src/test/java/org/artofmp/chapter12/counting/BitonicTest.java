/*
 * BitonicTest.java
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
public class BitonicTest extends TestCase {
  static final int TEST_SIZE = 256;
  static final int WIDTH = 4;
  
  public BitonicTest(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    TestSuite suite = new TestSuite(BitonicTest.class);
    
    return suite;
  }
  
  public void test2() throws Exception {
    Bitonic instance = new Bitonic(2);
    int[] map = new int[2];
    for (int i = 0; i < TEST_SIZE; i++) {
      map[instance.traverse(0)]++;
    }
    checkStep(map);
  }
  public void testTraverse() throws Exception {
    Bitonic instance = new Bitonic(WIDTH);
    int[] map = new int[WIDTH];
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
          for (int j : a) {
            System.out.printf("%d\t", j);
          }
          System.out.flush();
          throw new Exception();
        }
      }
    }
  }
  
}
