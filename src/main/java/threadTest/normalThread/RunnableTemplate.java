package threadTest.normalThread;

/**
 * Created by yek on 2017-4-12.
 */
public class RunnableTemplate implements Runnable{
    public void run() {
        try{
            while (!Thread.currentThread().isInterrupted()){
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            // thread sleep or wait;
        } finally {
            //clean up, if require;
        }
        // exit run(), terminates the thread.
    }
}
