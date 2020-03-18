package chapter4;

/**
 * Created by lili on 2017/4/27.
 */
public class Singleton1 {
    public static int STATUS = 1;
    private Singleton1(){
        System.out.println("Singleton is Create");
    }
    private static Singleton1 singleton1 = new Singleton1();
    public static Singleton1 getSingleton1(){
        return singleton1;
    }

    public static void main(String[] args) {
        System.out.println(Singleton1.STATUS);
    }
}

