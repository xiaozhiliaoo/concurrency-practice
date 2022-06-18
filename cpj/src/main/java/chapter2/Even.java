package chapter2;

/**
 * @author lili
 * @date 2022/6/19 1:42
 */
public class Even {                                    //  Do not use
    private int n = 0;

    public int next() { // POST?: next is always even
        ++n;
        ++n;
        return n;
    }
}