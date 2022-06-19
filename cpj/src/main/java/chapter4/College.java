/*
package chapter4;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;

*/
/**
 * @author lili
 * @date 2022/6/19 11:01
 *//*

public class College implements CSProcess {
    final static int N = 5;

    private final CSProcess action;

    College() {
        One2OneChannel[] lefts = One2OneChannel.create(N);
        One2OneChannel[] rights = One2OneChannel.create(N);
        One2OneChannel[] enters = One2OneChannel.create(N);
        One2OneChannel[] exits = One2OneChannel.create(N);

        Butler butler = new Butler(enters, exits);

        Philosopher[] phils = new Philosopher[N];
        for (int i = 0; i < N; ++i)
            phils[i] = new Philosopher(lefts[i], rights[i],
                    enters[i], exits[i]);

        Fork[] forks = new Fork[N];
        for (int i = 0; i < N; ++i)
            forks[i] = new Fork(rights[(i + 1) % N], lefts[i]);

        action = new Parallel(
                new CSProcess[] {
                        butler,
                        new Parallel(phils),
                        new Parallel(forks)
                });
    }

    public void run() { action.run(); }

    public static void main(String[] args) {
        new College().run();
    }


}
*/
