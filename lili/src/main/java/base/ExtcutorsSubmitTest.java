package base;

import java.util.concurrent.*;

/**
 * @author lili
 * @date 2020/5/27 22:37
 * @description
 * @notes
 */
public class ExtcutorsSubmitTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<Object> myTask = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new Exception("error1111111");
//            }
            }
        };

//        service.execute(()->{throw new RuntimeException("ddddddd");});

//        Callable<Object> myTask2 = new Callable<Object>() {
//            @Override
//            public Object call() throws Exception {
//                //throw new Exception("error1111111");
//                System.out.println("myTask2");
//                return new Object();
//            }
//        };
//
        Future<Object> error = service.submit(myTask);
//        error.cancel(true);
//        Future<Object> error2 = service.submit(myTask2);
//        Future<Object> error3 = service.submit(myTask);
//
//
//        try {
//            error.get();
//            error2.get();
//            error3.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            System.out.println("---------------");
//            error.cancel(true);
//            e.printStackTrace();
//        }


    }
}
