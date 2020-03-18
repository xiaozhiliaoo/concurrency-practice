package mytest;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @packgeName: mytest
 * @ClassName: MyLogWriter
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/6-20:05
 * @version: 1.0
 * @since: JDK 1.8
 */
public class MyLogWriter {
    private final BlockingQueue<String> queue;
    private final LoggerThread logger;
    private static final int CAPACITY = 1000;

    public MyLogWriter(Writer writer){
        this.queue = new LinkedBlockingQueue<>(CAPACITY);
        this.logger = new LoggerThread(writer);
    }

    public void start(){
        logger.start();
    }

    public void log(String mes) throws InterruptedException {
        queue.put(mes);
    }

    private class LoggerThread extends Thread{

        private final PrintWriter printWriter;

        public LoggerThread(Writer writer) {
            this.printWriter = new PrintWriter(writer,true);
        }

        @Override
        public void run() {
            try{
                while (true){
                    printWriter.print(queue.take());
                }
            }catch (InterruptedException ignored){

            }finally {
                printWriter.close();
            }
        }
    }
}
