package chapter3;

import EDU.oswego.cs.dl.util.concurrent.BrokenBarrierException;
import EDU.oswego.cs.dl.util.concurrent.Rendezvous;

/**
 * @author lili
 * @date 2022/6/19 10:13
 */
public class FillAndEmpty {                              // Incomplete
    static final int SIZE = 1024; // buffer size, for demo
    protected Rendezvous exchanger = new Rendezvous(2);

    protected byte readByte() {
        return 1; /* ... */
    }

    protected void useByte(byte b) { /* ... */ }

    public void start() {
        new Thread(new FillingLoop()).start();
        new Thread(new EmptyingLoop()).start();
    }

    class FillingLoop implements Runnable { // inner class
        public void run() {
            byte[] buffer = new byte[SIZE];
            int position = 0;

            try {
                for (; ; ) {

                    if (position == SIZE) {
                        buffer = (byte[]) (exchanger.rendezvous(buffer));
                        position = 0;
                    }

                    buffer[position++] = readByte();
                }
            } catch (BrokenBarrierException ex) {
            } // die
            catch (InterruptedException ie) {
            } // die
        }
    }

    class EmptyingLoop implements Runnable { // inner class
        public void run() {
            byte[] buffer = new byte[SIZE];
            int position = SIZE;  // force exchange first time through

            try {
                for (; ; ) {

                    if (position == SIZE) {
                        buffer = (byte[]) (exchanger.rendezvous(buffer));
                        position = 0;
                    }

                    useByte(buffer[position++]);
                }
            } catch (BrokenBarrierException ex) {
            } // die
            catch (InterruptedException ex) {
            } // die
        }
    }

}