package chapter3;

/**
 * @author lili
 * @date 2020/5/29 18:29
 * @description
 * @notes
 */
public class OptimisticBoundedCounter {
    private static final int MIN = Short.MIN_VALUE;
    private static final int MAX = Short.MAX_VALUE;
    private Long count_ = new Long(MIN);

    long value() {
        return count().longValue();
    }

    synchronized Long count() {
        return count_;
    }

    private synchronized boolean commit(Long oldc, Long newc) {
        boolean success = (count_ == oldc);
        if (success) count_ = newc;
        return success;
    }

    public void inc() throws InterruptedException {
        for (; ; ) { // retry-based
            if (Thread.interrupted())
                throw new InterruptedException();
            Long c = count();
            long v = c.longValue();
            if (v < MAX && commit(c, new Long(v + 1)))
                break;
            Thread.yield();
        }
    }

    public void dec() {

    }
}
