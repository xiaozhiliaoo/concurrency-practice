package chapter4.futuredemo;

/**
 * Created by lili on 2017/4/28.
 */
public class Client {
    public Data request(final String queryStr){
        final FutureData data = new FutureData();
        new Thread(){
            public void run(){
                RealData realData = new RealData(queryStr);
                data.setRealData(realData);
            }
        }.start();
        return data;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        Data data = client.request("name");
        System.out.println("请求完毕");
        System.out.println(data);
        //模拟在做其他的业务逻辑
        Thread.sleep(4000);
        System.out.println("真实数据：" + data.getResult());
    }
}
