package chapter4;

/**
 * Created by lili on 2017/4/27.
 */
public class LazySingleton {
    private LazySingleton(){
        System.out.println("is Created");
    }
    private static LazySingleton singleton = null;
    public synchronized static LazySingleton getSingleton(){
        if(singleton == null){
            singleton = new LazySingleton();
            return singleton;
        }
        return singleton;
    }


    protected static class CreateSingleton implements Runnable{

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName()+" is create "+ LazySingleton.getSingleton());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CreateSingleton createSingleton = new CreateSingleton();
        Thread[] t = new Thread[10];
        for (int i = 0; i < 10; i++) {
            t[i] = new Thread(createSingleton);
        }
        for (int i = 0; i < 10; i++) {
            t[i].start();
        }
        for (int i = 0; i < 10; i++) {
            t[i].join();
        }
        System.out.println("END....");
    }
}
