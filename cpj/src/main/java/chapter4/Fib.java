package chapter4;

import EDU.oswego.cs.dl.util.concurrent.FJTask;
import EDU.oswego.cs.dl.util.concurrent.FJTaskRunnerGroup;

/**
 * @author lili
 * @date 2020/5/30 22:02
 * @description
 * @notes
 */
public class Fib extends FJTask {

    static final int sequentialThreshold = 13; // for tuning
    volatile int number;                       // argument/result

    Fib(int n) {
        number = n;
    }

    int seqFib(int n) {
        if (n <= 1)
            return n;
        else
            return seqFib(n - 1) + seqFib(n - 2);
    }

    int getAnswer() {
        if (!isDone())
            throw new IllegalStateException("Not yet computed");
        return number;
    }

    public void run() {
        int n = number;

        if (n <= sequentialThreshold)       // base case
            number = seqFib(n);
        else {
            Fib f1 = new Fib(n - 1);          // create subtasks
            Fib f2 = new Fib(n - 2);

            coInvoke(f1, f2);                 // fork then join both

            number = f1.number + f2.number;   // combine results
        }
    }


    public static void main(String[] args) { // sample driver
        try {
            int groupSize = 2;    // 2 worker threads
            int num = 35;         // compute fib(35)
            FJTaskRunnerGroup group = new FJTaskRunnerGroup(groupSize);
            Fib f = new Fib(num);
            group.invoke(f);
            int result = f.getAnswer();
            System.out.println("Answer: " + result);
        } catch (InterruptedException ex) {
        } // die
    }
}

