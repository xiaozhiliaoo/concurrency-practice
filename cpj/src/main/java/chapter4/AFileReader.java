package chapter4;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author lili
 * @date 2022/6/19 10:44
 */
public class AFileReader implements FileReader {

    public void read(final String fn, final FileReaderClient c) {
        new Thread(new Runnable() {
            public void run() {
                doRead(fn, c);
            }
        }).start();
    }

    protected void doRead(String fn, FileReaderClient client) {
        byte[] buffer = new byte[1024]; // just for illustration
        try {
            FileInputStream s = new FileInputStream(fn);
            s.read(buffer);
            if (client != null) client.readCompleted(fn, buffer);
        } catch (IOException ex) {
            if (client != null) client.readFailed(fn, ex);
        }
    }
}
