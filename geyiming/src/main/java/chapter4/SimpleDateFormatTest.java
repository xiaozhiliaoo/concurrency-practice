package chapter4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lili on 2017/4/28.
 */
public class SimpleDateFormatTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //ThreadLocal封装日期类
//    static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<SimpleDateFormat>();

    public static class ParseDare implements Runnable{

        int i = 0;
        public ParseDare(int i){
            this.i = i;
        }
        @Override
        public void run() {
            try {
                Date d = sdf.parse("2017-03-29 19:29:"+i%60);
                System.out.println(i+":"+d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            es.submit(new ParseDare(i));
        }
    }
}
