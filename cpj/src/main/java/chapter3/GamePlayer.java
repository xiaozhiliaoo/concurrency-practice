package chapter3;

/**
 * @author lili
 * @date 2022/6/19 10:04
 */
public class GamePlayer implements Runnable {          // Incomplete
    protected GamePlayer other;
    protected boolean myturn = false;

    protected synchronized void setOther(GamePlayer p) {
        other = p;
    }

    synchronized void giveTurn() { // called by other player
        myturn = true;
        notify();                    // unblock thread
    }

    void releaseTurn() {
        GamePlayer p;
        synchronized (this) {
            myturn = false;
            p = other;
        }
        p.giveTurn(); // open call
    }

    synchronized void awaitTurn() throws InterruptedException {
        while (!myturn) wait();
    }

    void move() { /*... perform one move ... */ }

    public void run() {
        try {
            for (; ; ) {
                awaitTurn();
                move();
                releaseTurn();
            }
        } catch (InterruptedException ie) {
        } // die
    }

    public static void main(String[] args) {
        GamePlayer one = new GamePlayer();
        GamePlayer two = new GamePlayer();
        one.setOther(two);
        two.setOther(one);
        one.giveTurn();
        new Thread(one).start();
        new Thread(two).start();
    }
}