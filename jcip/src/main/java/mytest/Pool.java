package mytest;

import java.util.concurrent.Semaphore;

/**
 * @packgeName: mytest
 * @ClassName: Pool
 * @copyright: CopyLeft
 * @description:<描述>  jdk  Semaphore
 * @author: lili
 * @date: 2017/10/6-15:38
 * @version: 1.0
 * @since: JDK 1.8
 */
public class Pool {

    private static final int MAX_AVAILABLE = 100;
    //只能放100个对象
    private final Semaphore available = new Semaphore(MAX_AVAILABLE,true);

    public Object getItem() throws InterruptedException {
        available.acquire();
        return getNextAvailableItem();
    }

    public void putItem(Object x){
        if(markAsUnused(x)){
            available.release();
        }
    }

    protected Object[] items = new Object[300];
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    protected synchronized Object getNextAvailableItem() {

        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (!used[i]) {
                used[i] = true;
                return items[i];
            }
        }
        return null; // not reached
    }

    protected synchronized boolean markAsUnused(Object item) {
        for (int i = 0; i < MAX_AVAILABLE; ++i) {
            if (item == items[i]) {
                if (used[i]) {
                    used[i] = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        Pool pool = new Pool();

        for (int i = 0; i < 100; i++) {
            System.out.println("放入" + i);
            pool.putItem(i);
        }

        for (int i = 0; i < 100; i++) {
            System.out.println(pool.getItem());
        }
    }
}
