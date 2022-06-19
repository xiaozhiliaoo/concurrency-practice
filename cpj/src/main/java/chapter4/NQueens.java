package chapter4;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import EDU.oswego.cs.dl.util.concurrent.FJTaskRunnerGroup;

/**
 * @author lili
 * @date 2022/6/19 10:51
 */
public class NQueens extends FJTask {
    static int boardSize; // fixed after initialization in main
    // Boards are arrays where each cell represents a row,
    // and holds the column number of the queen in that row

    static class Result {          // holder for ultimate result
        private int[] board = null;  // non-null when solved

        synchronized boolean solved() {
            return board != null;
        }

        synchronized void set(int[] b) { // Support use by non-Tasks
            if (board == null) {
                board = b;
                notifyAll();
            }
        }

        synchronized int[] await() throws InterruptedException {
            while (board == null) wait();
            return board;
        }
    }

    static final Result result = new Result();

    public static void main(String[] args) {
        try {
            boardSize = 8; // ...;
            FJTaskRunnerGroup tasks = new FJTaskRunnerGroup(4);
            int[] initialBoard = new int[0]; // start with empty board
            tasks.execute(new NQueens(initialBoard));
            int[] board = result.await();
        } catch (InterruptedException ie) {
        }
        // ...
    }

    final int[] sofar;            // initial configuration

    NQueens(int[] board) {
        this.sofar = board;
    }

    public void run() {
        if (!result.solved()) {     // skip if already solved
            int row = sofar.length;

            if (row >= boardSize)     // done
                result.set(sofar);

            else {                    // try all expansions

                for (int q = 0; q < boardSize; ++q) {

                    // Check if queen can be placed in column q of next row
                    boolean attacked = false;
                    for (int i = 0; i < row; ++i) {
                        int p = sofar[i];
                        if (q == p || q == p - (row - i) || q == p + (row - i)) {
                            attacked = true;
                            break;
                        }
                    }

                    // If so, fork to explore moves from new configuration
                    if (!attacked) {
                        // build extended board representation
                        int[] next = new int[row + 1];
                        for (int k = 0; k < row; ++k) next[k] = sofar[k];
                        next[row] = q;
                        new NQueens(next).fork();
                    }
                }
            }
        }
    }
}
