package chapter3;

/**
 * @author lili
 * @date 2020/5/29 16:49
 * @description
 * @notes
 */
public class Terminator {

    // Try to kill; return true if known to be dead

    static boolean terminate(Thread t, long maxWaitToDie) {
        if (!t.isAlive()) return true;  // already dead

        // phase 1 -- graceful cancellation
        t.interrupt();
        try {
            t.join(maxWaitToDie);
        } catch (InterruptedException e) {
        } //  ignore

        if (!t.isAlive()) return true;  // success

        // phase 2 -- trap all security checks
        // theSecurityMgr.denyAllChecksFor(t); // a made-up method
        try {
            t.join(maxWaitToDie);
        } catch (InterruptedException ex) {
        }

        if (!t.isAlive()) return true;

        // phase 3 -- minimize damage
        t.setPriority(Thread.MIN_PRIORITY);
        return false;
    }

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            Thread thread1 = Thread.currentThread();
            try {
                boolean interrupted = thread1.isInterrupted();
                if(interrupted) {
                    System.out.println("I am interrupted");
                    //clean resource
                }
                System.out.println(thread1.getName() + " is sleep");
                //中断sleep
                Thread.sleep(5000000);
            } catch (InterruptedException e) {
                System.out.println("i am be interrupted");
                //clean resourse
                for (int i = 0; i < 5; i++) {
                    System.out.println("clean resourse:" + i);
                }
                System.out.println("is interrupted:"+ Thread.currentThread().isInterrupted());
                e.printStackTrace();
            }

            try {
                Thread.sleep(5000);
                System.out.println("--------");
               // System.out.println(thread1.isInterrupted());
                //清除中断状态，就不会中断了
               // Thread.interrupted();
//                System.out.println(interrupted);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();


        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Terminator.terminate(thread, 1000);
            }
        }).start();


    }
}
