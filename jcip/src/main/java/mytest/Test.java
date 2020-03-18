package mytest;

/**
 * @packgeName: mytest
 * @ClassName: Test
 * @copyright: CopyLeft
 * @description:<描述>
 * @author: lili
 * @date: 2017/10/7-2:51
 * @version: 1.0
 * @since: JDK 1.8
 */
class A{
    @Override
    public int hashCode() {
        return 1;
    }
}

public class Test {
    public static void main(String[] args) {
        A a = new A();
        A a1 = new A();
        System.out.println(a==a1);
    }
}
