package chapter4;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;

/**
 * @author lili
 * @date 2022/6/19 11:01
 */
public class Butler implements CSProcess {

    private final AltingChannelInput[] enters;
    private final AltingChannelInput[] exits;

    Butler(AltingChannelInput[] e, AltingChannelInput[] x) {
        enters = e;
        exits = x;
    }

    public void run() {
        int seats = enters.length;
        int nseated = 0;

        // set up arrays for select
        AltingChannelInput[] chans = new AltingChannelInput[2*seats];
        for (int i = 0; i < seats; ++i) {
            chans[i] = exits[i];
            chans[seats + i] = enters[i];
        }

        Alternative either = new Alternative(chans);
        Alternative exit = new Alternative(exits);

        for (;;) {
            // if max number are seated, only allow exits
            Alternative alt = (nseated <  seats-1)? either : exit;

            int i = alt.fairSelect();
            chans[i].read();

            // if i is in first half of array, it is an exit message
            if (i < seats) --nseated; else ++nseated;
        }
    }
}
