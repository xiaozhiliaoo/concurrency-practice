package chapter4;

import chapter3.Failure;

/**
 * @author lili
 * @date 2022/6/19 10:48
 */
public class ScheduledDisk implements Disk {
    protected final DiskTaskQueue tasks = new DiskTaskQueue();

    public void read(int c, byte[] b) throws Failure {
        readOrWrite(new DiskReadTask(c, b));
    }

    public void write(int c, byte[] b) throws Failure {
        readOrWrite(new DiskWriteTask(c, b));
    }

    protected void readOrWrite(DiskTask t) throws Failure {
        tasks.put(t);
        try {
            t.awaitCompletion();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // propagate
            throw new Failure(); // convert to failure exception
        }
        Failure f = t.getException();
        if (f != null) throw f;
    }

    public ScheduledDisk() {     // construct worker thread
        new Thread(new Runnable() {
            public void run() {
                try {
                    for (; ; ) {
                        tasks.take().run();
                    }
                } catch (InterruptedException ex) {
                } // die
            }
        }).start();
    }
}