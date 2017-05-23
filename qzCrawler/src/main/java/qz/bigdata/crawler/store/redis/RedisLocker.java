package qz.bigdata.crawler.store.redis;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.configuration.GlobalOption;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 要实现分布式的锁，对编程模式有不同的选择
 * 一种是对等式的，也就是说，对同一个功能块来说，不同节点上的程序都是一致的，包括锁的创建和使用。
 * 一种是非对等式的，也就是说，对于同一个功能块来说，有一个进程或者线程专门负责创建锁，其它节点上使用锁的程序是对等的。也就是说，程序非为创建锁和使用锁两个部分，创建锁的只能被执行一次，使用锁的可以多次执行。
 * 如果使用对等式的，那么在创建锁的时候就要求系统能提供分布式的锁，避免重复创建。
 * 如果系统没有分布式的锁，那么可以使用已经创建好的一个全局的分布式的锁来避免重复创建相同的锁，但是这样对于一个大型的分布式并发服务来说，性能是一个问题。并且用户得正确的释放锁，要不然会造成其他创建锁被阻塞。
 * 使用非对等式的，那么由用户负责创建锁的唯一性。如果用户对一个锁执行了两次创建，那么锁的数量就不正确了。（初始值会是2倍）
 */

/**
 * Created by fys on 2015/4/22.
 */
public class RedisLocker {

    private String key;
//    private PooledRedisClient client;

//    private JedisPool pool = null;
//    private String password;
//    private static final String lockKeyPrefix = "lock_";

    private static int count = 0;
    private static int num = 0;

    private static final Logger logger = Logger.getLogger(RedisLocker.class);

    public RedisLocker(String key){
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(1000);
//        config.setMaxIdle(20 + 1);
//        config.setMaxWait(20000l);
//        this.client = new PooledRedisClient(config, GlobalOption.redisIP, GlobalOption.redisPort, GlobalOption.redisPassword);
        this.key = key;
    }

    public static boolean generateRedisLocker(String key, int permits){
        return RedisLockUtility.prepareLock(key, permits);
    }

//    private Jedis getJedis(){
//        Jedis cacheClient = pool.getResource();
//        if( !password.equals("")){
//            cacheClient.auth(password);
//        }
//        //RedisDBManager rdbm = new RedisDBManager(cacheClient);
//        //cacheClient.select( 0 );// rdbm.getProjectId( GlobalOption.redisDbName));
//        return cacheClient;
//    }
//    private String getLockKey(String lockKey){
//        return lockKeyPrefix + lockKey;
//    }

    public long len(){
        return RedisLockUtility.len(this.key);
    }

//    public void len(String key){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        try {
//            cacheClient = getJedis();
//            return cacheClient.llen(key);
//        } catch (JedisConnectionException e) {
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            return -1;
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//    }

//    private boolean exist(String key){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        try {
//            cacheClient = getJedis();
//            return cacheClient.exists(key);
//        } catch (JedisConnectionException e) {
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//        return false;
//    }

//    private boolean push(String lockKey, String value){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        String key = getLockKey(lockKey);
//        try {
//            cacheClient = getJedis();
//            cacheClient.lpush(key, value);
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            return false;
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//        return true;
//    }

//    private boolean push(String lockKey, List<String> values){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        String key = getLockKey(lockKey);
//        try {
//            cacheClient = getJedis();
//            for(String value : values) {
//                cacheClient.lpush(key, value);
//            }
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            return false;
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//        return true;
//    }

//    public String bpop(String lockKey) {
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        String key = getLockKey(lockKey);
//        try {
//            cacheClient = getJedis();
//            List<String> ret = cacheClient.brpop(0, key);
//            return ret.get(1);
//
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            //todo:应该抛出异常么？我觉得应该
//            throw e;
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//    }

    //创建一个命名分布式锁
//    public static boolean prepareLock(String lockKey){
//        return prepareLock(lockKey, 1);
//    }
    //创建一个命名分布式信号量
//    public static boolean prepareLock(String lockKey, int permits){
//        PooledRedisClient client =
//        String key = getLockKey(lockKey);
//        if(this.exist(key)){
//            return false;//
//        }
//        else{
//            List<String> lockValues = new ArrayList<String>(permits);
//            for(int i=0; i<permits;i++) {
//                lockValues.add(String.valueOf(i));
//            }
//            if(this.push(key, lockValues) == false){
//                this.delete(key);
//                return false;
//            }
//            return true;
//        }
//    }


    //由系统创建一个分布式锁，并且返回分布式锁的关键字，但是这种是不能跨分布式的，除非把key传给另外的节点
//    public static String prepareLock(){
//        return prepareLock(1);
//    }

    //创建一个分布式信号量，返回信号量的关键字
//    public static String prepareLock(int permits){
//        //如果是0，则相当于变成了wait，notify模式
//        if(permits < 1) permits = 0;
//        UUID uuid = UUID.randomUUID();
//        String key = getLockKey(uuid.toString());
//        if(this.exist(key)){
//            return prepareLock(permits); //已经被其它占用.
//        }
//        else{
//            //初始化信号量操作
//            List<String> lockValues = new ArrayList<String>(permits);
//            for(int i=0; i<permits;i++) {
//                lockValues.add(String.valueOf(i));
//            }
//            if(this.push(key, lockValues) == false){
//                this.delete(uuid.toString());
//                return null;
//            }
//            return uuid.toString();
//        }
//    }

//    public static void delete(String lockKey) {
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        String key = getLockKey(lockKey);
//        try {
//            cacheClient = getJedis();
//            cacheClient.del(key);
//
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            //todo:应该抛出异常么？我觉得应该
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//    }

    public String acquire() throws Exception{
        return RedisLockUtility.acquire(this.key);
    }

    public void release(String value) throws Exception{
        RedisLockUtility.release(this.key, value);
    }

    public String getKey(){
        return this.key;
    }

    public static void main(String[] args){

        final String lockKey = "\\lock\\test\\redislocker";
        RedisLocker locker = new RedisLocker(lockKey);
//        final String lockKey = locker.prepareLock(1);
//        if(lockKey == null) return;
        System.out.println(lockKey + " : " + new Date());
        if(true) {
            for (int i = 0; i < 500; i++) {
                Thread t = new Thread() {
                    public void run() {
                        int c = RedisLocker.count++;

                        try {
                            //Thread.sleep((long) (1000 * Math.random()));
                        }
                        catch (Exception ex){


                        }
                        for(int i = 0; i < 10; i++) {
                            RedisLocker locker = new RedisLocker(lockKey);
//                        String value = locker.acquire(lockKey);
                            try {
                                locker.acquire();
                            } catch (Exception ex) {
                                System.out.println(ex.getMessage());
                                return;
                            }
                            System.out.println(locker.getKey() + ": get in : " + c);

                            try {
                                //todo: 完成业务逻辑
                                //Thread.sleep((long) (10000 * Math.random()));
                            } catch (Exception ex) {

                            }
                            System.out.println(locker.getKey() + ": leave : " + c + " : " + new Date());
                            try {
                                locker.release("fys");
                                Thread.sleep(100);
                            } catch (Exception ex) {
                                System.out.println("release error: lock may be invalid. " + ex.getMessage());
                                return;
                            }
                        }
                    }
                };
                t.start();
            }
        }
    }
}
