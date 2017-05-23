package qz.bigdata.crawler.store.redis;

import qz.bigdata.crawler.configuration.GlobalOption;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fys on 2015/5/3.
 */
public class RedisLockUtility {
    public static PooledRedisClient client;//
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(60000);
        config.setMaxIdle(5000);
        config.setMaxWait(20000l);
        client = new PooledRedisClient(config, GlobalOption.redisIP, GlobalOption.redisPort, GlobalOption.redisPassword);
    }

    public static boolean generateRedisLocker(String key){
        return prepareLock(key, 1);
    }
    public static boolean generateRedisLocker(String key, int permits){
        return prepareLock(key, permits);
    }

    public static boolean generateRedisBarrier(String key) throws Exception{
        if(client.exist(key)){
            throw new Exception(key + " is already used.");
        }
        client.set(key, "barrier");
        prepareLock(key + "\\help_lock", 1);
        prepareLock(key + "\\lock_queue", 0);
        prepareLock(key + "\\back_queue", 0);
        return true;
    }

    public static boolean clearRedisBarrier(String key){
        try {
            client.delete(key);
            client.delete(key + "\\help_lock");
            client.delete(key + "\\lock_queue");
            client.delete(key + "\\back_queue");
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    public static boolean generateRedisCountDownEvent(String key, int initialCount) throws Exception{
        if(client.exist(key)){
            throw new Exception(key + " is already used.");
        }
        client.set(key, "count down " + String.valueOf(initialCount));
        prepareLock(key + "\\help_lock", 1);
        prepareLock(key + "\\lock_queue", 0);
        prepareLock(key + "\\back_queue", 0);
        client.set(key + "\\count", String.valueOf(initialCount));
        return true;
    }

    public static boolean clearRedisCountDownEvent(String key){
        try {
            client.delete(key);
            client.delete(key + "\\help_lock");
            client.delete(key + "\\lock_queue");
            client.delete(key + "\\back_queue");
            client.delete(key + "\\count");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static boolean prepareLock(String key, int permits){
        Jedis jedis = null;//client.getJedis();
        JedisPool pool = client.getPool();
        try {
            jedis = pool.getResource();
            client.auth(jedis);
            if (jedis.exists(key)) {
                return false;
            }
            List<String> lockValues = new ArrayList<String>(permits);
            for (int i = 0; i < permits; i++) {
                lockValues.add(String.valueOf(i));
            }
            for(String value : lockValues) {
                jedis.lpush(key, value);
            }
            return true;
        }
        catch (Exception ex){
            if(jedis != null)
                pool.returnBrokenResource(jedis);
        }
        finally {
            if(jedis != null)
                pool.returnResource(jedis);
        }
        return false;
    }

    public static long len(String key){
        Jedis jedis = null;//client.getJedis();
        JedisPool pool = client.getPool();
        try {
            jedis = pool.getResource();
            client.auth(jedis);
            return jedis.llen(key);
        }
        catch (Exception ex){
            if(jedis != null)
                pool.returnBrokenResource(jedis);
        }
        finally {
            if(jedis != null)
                pool.returnResource(jedis);
        }
        return -1;
    }

    public static String acquire(String key) throws Exception{
        return client.bpop(key);// RedisLockUtility.bpop(key);
    }
    public static void release(String key, String value) throws Exception{
        client.push(key, value);//        return RedisLockUtility.push(key, value);
    }

//    public static String bpop(String key) {
//        Jedis jedis = client.getJedis();
//        JedisPool pool = client.getPool();
//        try {
//            List<String> ret = jedis.brpop(0, key);
//            return ret.get(1);
//        } finally {
//            pool.returnResource(jedis);
//        }
//    }
//
//    public static boolean push(String key, String value){
//        Jedis jedis = client.getJedis();
//        JedisPool pool = client.getPool();
//        try {
//            jedis.lpush(key, value);
//        } finally {
//            pool.returnResource(jedis);
//        }
//        return true;
//    }
}
