package chapter4;

import chapter3.Failure;

/**
 * @author lili
 * @date 2022/6/19 10:47
 */
public class DiskReadTask extends DiskTask {
    DiskReadTask(int c, byte[] b) {
        super(c, b);
    }

    void access() throws Failure { /* ... raw read ... */ }
}
