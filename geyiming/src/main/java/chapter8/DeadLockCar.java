package chapter8;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lili on 2017/5/21.
 */
public class DeadLockCar extends Thread {

    // jstack -l pid
    protected Object myDirect;

    static ReentrantLock south = new ReentrantLock();
    static ReentrantLock north = new ReentrantLock();
    static ReentrantLock west = new ReentrantLock();
    static ReentrantLock east = new ReentrantLock();

    public DeadLockCar(Object obj) {
        this.myDirect = obj;
        if(myDirect == south){
            this.setName("south");
        }
        if(myDirect == north){
            this.setName("north");
        }
        if(myDirect == west){
            this.setName("west");
        }
        if(myDirect == east){
            this.setName("east");
        }
    }

    @Override
    public void run() {
        if(myDirect  == south){
            try{
                west.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                south.lockInterruptibly();
                System.out.println("Car to South has Passed");
            }catch(InterruptedException e){
                System.out.println("Car to South is killed");
            }finally {
                if(west.isHeldByCurrentThread()){
                    west.unlock();
                }
                if(south.isHeldByCurrentThread()){
                    south.unlock();
                }
            }
        }
        if(myDirect  == north){
            try{
                east.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                north.lockInterruptibly();
                System.out.println("Car to north has Passed");
            }catch(InterruptedException e){
                System.out.println("Car to north is killed");
            }finally {
                if(north.isHeldByCurrentThread()){
                    north.unlock();
                }
                if(east.isHeldByCurrentThread()){
                    east.unlock();
                }
            }
        }
        if(myDirect  == west){
            try{
                north.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                west.lockInterruptibly();
                System.out.println("Car to west has Passed");
            }catch(InterruptedException e){
                System.out.println("Car to west is killed");
            }finally {
                if(north.isHeldByCurrentThread()){
                    north.unlock();
                }
                if(west.isHeldByCurrentThread()){
                    west.unlock();
                }
            }
        }
        if(myDirect  == east){
            try{
                south.lockInterruptibly();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                east.lockInterruptibly();
                System.out.println("Car to east has Passed");
            }catch(InterruptedException e){
                System.out.println("Car to east is killed");
            }finally {
                if(east.isHeldByCurrentThread()){
                    east.unlock();
                }
                if(south.isHeldByCurrentThread()){
                    south.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadLockCar car2south = new DeadLockCar(south);
        DeadLockCar car2north = new DeadLockCar(north);
        DeadLockCar car2west = new DeadLockCar(west);
        DeadLockCar car2east = new DeadLockCar(east);
        car2south.start();
        car2north.start();
        car2west.start();
        car2east.start();
        Thread.sleep(1000);
    }
}
