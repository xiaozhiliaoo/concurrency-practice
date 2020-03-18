package chapter8;

/**
 * Created by lili on 2017/5/28.
 */

public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        Point a = new Point(0,0);
        Point b = new Point(1,1);
        Point c = new Point(5,3);
        Point d = new Point(9,8);
        Point e = new Point(6,7);
        Point f = new Point(3,9);
        Point g = new Point(4,8);
        Line aLine = new Line(a,b);
        Line bLine = new Line(a,c);
        Line cLine = new Line(d,e);
        Line dLine = new Line(f,g);
        a=null;
        b=null;
        c=null;
        d=null;
        e=null;
        Thread.sleep(1000000);
    }
}
