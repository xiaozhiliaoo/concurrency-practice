package chapter4;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;

/**
 * @author lili
 * @date 2022/6/19 11:01
 */
public class Philosopher implements CSProcess {

    private final ChannelOutput leftFork;
    private final ChannelOutput rightFork;
    private final ChannelOutput enter;
    private final ChannelOutput exit;

    Philosopher(ChannelOutput l, ChannelOutput r,
                ChannelOutput e, ChannelOutput x) {
        leftFork = l;
        rightFork = r;
        enter = e;
        exit = x;
    }

    public void run() {

        for (;;) {

            think();

            enter.write(null);          // get seat
            leftFork.write(null);       // pick up left
            rightFork.write(null);      // pick up right

            eat();

            leftFork.write(null);       // put down left
            rightFork.write(null);      // put down right
            exit.write(null);           // leave seat

        }

    }

    private void eat() {}
    private void think() {}

}
