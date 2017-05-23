package qz.bigdata.crawler.store.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fys on 2015/4/30.
 */
public class RedisCountdownEvent {
    private RedisLocker helpLocker;
    private RedisLocker mainLocker;
    private RedisLocker backLocker;

    private RedisLocker locker;
    private String lockQueueKey;
    private String backQueueKey;
    private String helperLockKey;
    private String countKey;
    final private int initialCount;
    private int waitCount = 0;

    //todo:可增加一个参数，表示是否可以重入，不可重入的在countdown为0的时候唤醒等待线程后则释放所有资源。
    public RedisCountdownEvent(String key, int initialCount){
        if(initialCount <= 1) throw new IllegalArgumentException("initialCount must > 1.");
        //todo:初始化countKey，并设置值为initialCount
        this.initialCount = initialCount;
        this.countKey = key + "\\count";
        this.helpLocker = new RedisLocker(key + "\\help_lock");
        this.mainLocker = new RedisLocker(key + "\\lock_queue");
        this.backLocker = new RedisLocker(key + "\\back_queue");

//        locker = new RedisLocker();
//        this.helperLockKey = key + "\\help_lock";// locker.prepareLock();
//        this.lockQueueKey = key + "\\lock_queue";//locker.prepareLock(0);
//        this.backQueueKey = key + "\\back_queue";//locker.prepareLock(0);
    }


    public void countdown() throws Exception{
        //todo: 对countKey的值--
        //todo:当countKey的值==0时，调用netNotifyAll唤醒所有等待的线程。
        PooledRedisClient client = RedisLockUtility.client;
        if(client.decr(this.countKey) == 0){
            this.netNotifyAll();
//            client.set(this.countKey, String.valueOf(this.initialCount));
        }
    }

    public void netWait() throws Exception{
        this.helpLocker.acquire();
        try{
            this.backLocker.release(String.valueOf(++this.waitCount));
        }
        finally {
            this.helpLocker.release("fys");
        }
        this.mainLocker.acquire();
    }

    private void netNotify() throws Exception{
        this.netNotify(1);
    }

    private void netNotifyAll() throws Exception{
        String value;
        List<String> list;
        long len;
        this.helpLocker.acquire();
        try{
            len = this.backLocker.len();
            if( len == 0){
                return;
            }
            list = new ArrayList<String>();
            //可以直接清空此队列
            for(int i=0; i<len; i++) {
                list.add(this.backLocker.acquire());
            }
        }
        finally {
            this.helpLocker.release("fys");
        }
        for(String s : list) {
            --waitCount;
            this.mainLocker.release(s);
        }
    }

    private void netNotify(long notifyNumber) throws Exception{
        String value;
        List<String> list;
        long len, count;
        this.helpLocker.acquire();// this.locker.acquire(this.helperLockKey);
        try{
            len = this.backLocker.len();// this.locker.len(this.backQueueKey);
            if( len == 0){
                return;
            }
            list = new ArrayList<String>();
            count = len < notifyNumber ? len : notifyNumber;
            for(int i=0; i< count; i++) {
                list.add(this.backLocker.acquire());//this.locker.acquire(this.backQueueKey));
            }
        }
        finally {
            this.helpLocker.release("fys"); //this.locker.release(this.helperLockKey, "fys");
        }
        for(String s : list) {
            --waitCount;
            this.mainLocker.release(s);// this.locker.release(this.lockQueueKey, s);
        }
    }
}
