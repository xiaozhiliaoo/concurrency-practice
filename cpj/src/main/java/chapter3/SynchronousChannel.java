package chapter3;

/**
 * @author lili
 * @date 2020/5/29 19:30
 * @description
 * @notes
 */
public class SynchronousChannel {
    Object item_ = null;
    boolean putting_ = false;//disable multiple puts

    synchronized void put(Object e) {
        if (e == null) return;
        while (putting_) {
            try {
                wait();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        putting_ = true;
        item_ = e;
        notifyAll();
        while (item_ != null) {
            try {
                wait();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        putting_ = false;
        notifyAll();
    }

    synchronized Object take() {
        while (item_ == null) try {
            wait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object e = item_;
        item_ = null;
        notifyAll();
        return e;
    }
}
