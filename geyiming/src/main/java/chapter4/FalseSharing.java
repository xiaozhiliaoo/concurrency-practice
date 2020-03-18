package chapter4;

/**
 * Created by lili on 2017/4/28.
 */
public class FalseSharing implements Runnable{
    public final static int NUM_THREAD = 2;
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREAD];

    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("durations:" +(System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread [] threads = new Thread[NUM_THREAD];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for(Thread t:threads){
            t.start();
        }
        for(Thread t:threads){
            t.join();
        }
    }

    public void run(){
        long i = ITERATIONS + 1;
        while(0 != --i){
            longs[arrayIndex].value = i;
        }
    }

    public final static class VolatileLong{
        public volatile long value = 0L;
        public long p1,p2,p3,p4,p5,p6,p7;
    }
}
