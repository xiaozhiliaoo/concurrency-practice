package chapter4.futuredemo;

/**
 * Created by lili on 2017/4/28.
 */
public class RealData implements Data{
    protected final String result;

    public RealData(String para) {
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
        result = sb.toString();
    }

    @Override
    public String getResult() {
        return result;
    }
}
