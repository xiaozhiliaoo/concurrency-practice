package org.example.ex24;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lili
 * @date 2020/4/19 22:29
 * @description
 * @notes
 */
public class MyCountDownTimer extends CountDownTimer {

    public MyCountDownTimer(Lock lock, long millisInFuture, long countDownInterval) {
        super(lock, millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        System.out.println(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        System.out.println("onFinish");
    }

    public static void main(String[] args) {
        MyCountDownTimer timer = new MyCountDownTimer(new ReentrantLock(),100000,1);
        timer.start();
    }
}
