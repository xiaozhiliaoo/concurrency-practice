package chapter4;


/**
 * @author lili
 * @date 2022/6/19 10:54
 */
public class JacobiSegment implements Runnable {        // Incomplete
    // These are same as in Leaf class version:
    static final double EPSILON = 0.001;
    double[][] A;
    double[][] B;
    final int firstRow;
    final int lastRow;
    final int firstCol;
    final int lastCol;
    volatile double maxDiff;
    int steps = 0;

    void update() { /* Nearly same as Leaf.run */ }

    final CyclicBarrier bar;
    final JacobiSegment[] allSegments; // needed for convergence check
    volatile boolean converged = false;

    JacobiSegment(double[][] A, double[][] B,
                  int firstRow, int lastRow,
                  int firstCol, int lastCol,
                  CyclicBarrier b, JacobiSegment[] allSegments) {
        this.A = A;
        this.B = B;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.firstCol = firstCol;
        this.lastCol = lastCol;
        this.bar = b;
        this.allSegments = allSegments;
    }

    public void run() {
        try {
            while (!converged) {
                update();
                int myIndex = bar.barrier(); // wait for all to update
                if (myIndex == 0) convergenceCheck();
                bar.barrier();             // wait for convergence check
            }
        } catch (Exception ex) {
            // clean up ...
        }
    }

    void convergenceCheck() {
        for (int i = 0; i < allSegments.length; ++i)
            if (allSegments[i].maxDiff > EPSILON) return;
        for (int i = 0; i < allSegments.length; ++i)
            allSegments[i].converged = true;
    }
}