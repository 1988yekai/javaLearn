package qz.bigdata.crawler.store.redis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fys on 2015/4/30.
 */
public class RedisBarrier {
    final private int number;
    private RedisLocker helpLocker;
    private RedisLocker mainLocker;
    private RedisLocker backLocker;
//    private String lockQueueKey;
//    private String backQueueKey;
//    private String helperLockKey;
    private int waitCount = 0;

    public RedisBarrier(String key, int barrierNumber){
        if(barrierNumber <= 1) throw new IllegalArgumentException("barrierNumber must > 1.");
        this.number = barrierNumber;
//        this.helperLockKey = key + "\\help_lock";// locker.prepareLock();
//        this.lockQueueKey = key + "\\lock_queue";//locker.prepareLock(0);
//        this.backQueueKey = key + "\\back_queue";//locker.prepareLock(0);
        this.helpLocker = new RedisLocker(key + "\\help_lock");
        this.mainLocker = new RedisLocker(key + "\\lock_queue");
        this.backLocker = new RedisLocker(key + "\\back_queue");
    }

    public static boolean generateRedisBarrier(String key){
        String helperLockKey = key + "\\help_lock";// locker.prepareLock();
        String lockQueueKey = key + "\\lock_queue";//locker.prepareLock(0);
        String backQueueKey = key + "\\back_queue";//locker.prepareLock(0);
        RedisLockUtility.prepareLock(helperLockKey, 1);
        RedisLockUtility.prepareLock(lockQueueKey, 0);
        RedisLockUtility.prepareLock(backQueueKey, 0);
        return true;
    }

    public void signalAndWait() throws Exception{
        this.helpLocker.acquire();
        try{
            this.backLocker.release(String.valueOf(++this.waitCount));
            if(this.backLocker.len() == number){
                this.signalAll();
            }
        }
        finally {
            this.helpLocker.release("fys");
        }
        this.mainLocker.acquire();
    }

    private void signalAll() throws Exception{
        long len = this.backLocker.len();// RedisLockUtility.len(this.backQueueKey);
        if( len == 0){
            return;
        }
        List<String> list = new ArrayList<String>();
        //可以直接清空此队列
        for(int i=0; i<len; i++) {
            list.add(this.backLocker.acquire());
        }
        for(String s : list) {
            --this.waitCount;
            this.mainLocker.release(s);
        }
    }
}
