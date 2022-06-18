package chapter3;

public interface BoundedCounter {

    static final long MIN = 0;  // minimum allowed value

    static final long MAX = 10; // maximum allowed value


    long count();         // INV:  MIN <= count() <= MAX
    // INIT: count() == MIN

    void inc();           // only allowed when count() < MAX

    void dec();           // only allowed when count() > MIN

}
