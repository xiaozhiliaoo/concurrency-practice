package chapter4;


import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;

/**
 * @author lili
 * @date 2022/6/19 10:59
 */
public class Fork implements CSProcess {

    private final AltingChannelInput[] fromPhil;

    Fork(AltingChannelInput l, AltingChannelInput r) {
        fromPhil = new AltingChannelInput[]{l, r};
    }

    public void run() {
        Alternative alt = new Alternative(fromPhil);

        for (; ; ) {
            int i = alt.select();   // await message from either
            fromPhil[i].read();     // pick up
            fromPhil[i].read();     // put down
        }

    }
}
