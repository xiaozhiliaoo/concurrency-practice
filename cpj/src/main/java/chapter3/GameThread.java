package chapter3;

/**
 * @author lili
 * @date 2020/5/29 19:10
 * @description Thread-Specific Storage
 * @notes
 */
public class GameThread extends Thread {
    private long movementDelay_ = 3;

    static GameThread currentGameThread() {
        return (GameThread) (Thread.currentThread());
    }

    static long getDelay() {
        return currentGameThread().movementDelay_;
    }

    static void setDelay(long t) {
        currentGameThread().movementDelay_ = t;
    }

    public static void main(String[] args) {
        GameThread gameThread = new GameThread();
        GameThread.getDelay();

        gameThread.start();
        GameThread.getDelay();
    }
}

class Ball { // ...
    void move() { // ...
        try {
            Thread.sleep(GameThread.getDelay());
        } catch (InterruptedException e) {
            e.printStackTrace();
        };
    }
}