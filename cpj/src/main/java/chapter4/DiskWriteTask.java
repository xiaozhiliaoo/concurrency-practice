package chapter4;

import chapter3.Failure;

/**
 * @author lili
 * @date 2022/6/19 10:48
 */
public class DiskWriteTask extends DiskTask {
    DiskWriteTask(int c, byte[] b) {
        super(c, b);
    }

    void access() throws Failure { /* ... raw write ... */ }
}
