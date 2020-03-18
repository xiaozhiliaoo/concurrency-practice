package net.jcip.examples;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @packgeName: net.jcip.examples
 * @ClassName: ProducerConsumer2
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/6-11:42
 * @version: 1.0
 * @since: JDK 1.8
 */
public class ProducerConsumer2 {

    static class FileCrawler implements Runnable{

        private final BlockingQueue<File> fileQueue;
        private final FileFilter fileFilter;
        private final File root;

        public FileCrawler(BlockingQueue<File> fileQueue, FileFilter fileFilter, File root) {
            this.fileQueue = fileQueue;
            this.fileFilter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || fileFilter.accept(f);
                }
            };
            this.root = root;
        }

        private boolean alreadyIndexed(File f){
            return false;
        }

        @Override
        public void run(){

            try {
                crawl(root);
            } catch (InterruptedException e) {
//                throw new InterruptedIOException();
                Thread.currentThread().interrupt();
            }
        }

        private void crawl(File root) throws InterruptedException {
            File[] entries = root.listFiles(fileFilter);
            if (entries != null) {
                for (File entry : entries) {
                    if(entry.isDirectory()){
                        crawl(entry);
                    }else if(!alreadyIndexed(entry)){
                        fileQueue.put(entry);
                    }
                }
            }
        }
    }

    static class Indexer implements Runnable{

        private final BlockingQueue<File>queue;

        public Indexer(BlockingQueue<File> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true){
                    indexFile(queue.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void indexFile(File file){
            //
            System.out.println("Index file "+ file.getAbsolutePath());
        }
    }


    private static final int BOUND = 10;
    private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();

    public static void startIndexing(File[] roots){
        BlockingQueue<File>queue = new LinkedBlockingDeque<File>(BOUND);

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };

        for(File root:roots){
            new Thread(new ProducerConsumer2.FileCrawler(queue, filter, root)).start();
        }

        for (int i = 0; i < N_CONSUMERS; i++) {
            new Thread(new Indexer(queue)).start();
        }
    }

    public static void main(String[] args) {
        startIndexing(new File[]{new File("D:\\AstahSpace")});
    }

}
