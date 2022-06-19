package chapter4;

import java.io.IOException;

/**
 * @author lili
 * @date 2022/6/19 10:44
 */
public class FileApplication implements FileReaderClient {
    private String[] filenames;
    private int currentCompletion; // index of ready file

    public synchronized void readCompleted(String fn, byte[] d) {
        // wait until ready to process this callback
        while (!fn.equals(filenames[currentCompletion])) {
            try {
                wait();
            } catch (InterruptedException ex) {
                return;
            }
        }
        // ... process data...
        // wake up any other thread waiting on this condition:
        ++currentCompletion;
        notifyAll();
    }

    public synchronized void readFailed(String fn, IOException e) {
        // similar...
    }

    public synchronized void readfiles() {
        AFileReader reader = new AFileReader();
        currentCompletion = 0;
        for (int i = 0; i < filenames.length; ++i)
            reader.read(filenames[i], this);
    }
}
