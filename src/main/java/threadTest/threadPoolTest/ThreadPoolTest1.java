package threadTest.threadPoolTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yek on 2017-3-30.
 */
public class ThreadPoolTest1 {
    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " " + index);
                        Thread.sleep(4000);

                    } catch (InterruptedException e) {
//                        e.printStackTrace();
                        System.out.println("error!");
                    }
                }
            });
            System.out.println("---> " + i);


        }
//        fixedThreadPool.shutdown();//线程跑完后关闭
        // Wait a while for existing tasks to terminate
        try {
            if (!fixedThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                fixedThreadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!fixedThreadPool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException e) {
            // (Re-)Cancel if current thread also interrupted
            fixedThreadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            System.out.println("==over==");
        }
    }
}
