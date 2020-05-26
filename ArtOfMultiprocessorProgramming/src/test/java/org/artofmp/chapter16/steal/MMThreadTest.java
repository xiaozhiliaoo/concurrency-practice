/*
 * MMThreadTest.java
 * JUnit based test
 *
 * Created on January 21, 2006, 10:59 PM
 */

package org.artofmp.chapter16.steal;

import junit.framework.*;

/**
 * @author mph
 */
public class MMThreadTest extends TestCase {

    public MMThreadTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(MMThreadTest.class);

        return suite;
    }

    /**
     * Test of run method, of class steal.Fib.
     */
    public void testRun() {
        System.out.println("run");

        double[][] a = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double[][] b = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        MMThread instance = new MMThread(a, b);
        instance.multiply();
        double[][] c = instance.c;

        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(c[i][j], a[i][j]);
            }
        }
    }
}
