package chapter4;

/**
 * @author lili
 * @date 2020/5/30 22:07
 * @description
 * @notes
 */
public class Leaf extends JTree {
    private final double[][] A;
    private final double[][] B;
    private final int loRow;
    private final int hiRow;
    private final int loCol;
    private final int hiCol;
    private int steps = 0;

    Leaf(double[][] A, double[][] B,
         int loRow, int hiRow, int loCol, int hiCol) {
        this.A = A;
        this.B = B;
        this.loRow = loRow;
        this.hiRow = hiRow;
        this.loCol = loCol;
        this.hiCol = hiCol;
    }

    public synchronized void run() {
        boolean AtoB = (steps++ % 2) == 0;
        double[][] a = (AtoB) ? A : B;
        double[][] b = (AtoB) ? B : A;
        double md = 0.0;
        for (int i = loRow; i <= hiRow; ++i) {
            for (int j = loCol; j <= hiCol; ++j) {
                b[i][j] = 0.25 * (a[i - 1][j] + a[i][j - 1] +
                        a[i + 1][j] + a[i][j + 1]);
                md = Math.max(md, Math.abs(b[i][j] - a[i][j]));
            }
        }
        maxDiff = md;
    }
}