package chapter4.callable;

import java.util.concurrent.Callable;

/**
 * Created by lili on 2017/4/28.
 */
public class RealData implements Callable<String>{
    private String para;

    public RealData(String para) {
        this.para = para;
    }

    @Override
    public String call() throws Exception {
        //获取真实数据会很慢
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(para);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  sb.toString();
    }
}
