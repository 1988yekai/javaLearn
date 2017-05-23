package qz.bigdata.crawler.utility;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
import qz.bigdata.crawler.configuration.GlobalOption;

import java.util.List;

/**
 * Created by fys on 2015/1/15.
 * 需要考虑的问题：并发，序列化，跨JVM，锁等
 */
public class RedisUtility {
    private static Logger logger = Logger.getLogger(RedisUtility.class);
    private static JedisPool pool = null;
    public static synchronized JedisPool getPool(){
        return pool;
    }
    static{

        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxActive(60);

        config.setMaxIdle(20+1);

        config.setMaxWait(2000l);

        pool = new JedisPool(config, GlobalOption.redisIP, GlobalOption.redisPort);

    }
    public static void setData(String key, String value)
    {
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;

        try {
            cacheClient = pool.getResource();
            cacheClient.select(GlobalOption.redisDB);

            cacheClient.set(key, value);
            // do redis opt by instance
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
    }
    public static void setData(String key, List<String> values)
    {
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;

        try {
            cacheClient = pool.getResource();
            cacheClient.select(GlobalOption.redisDB);

            for(String value : values) {
                cacheClient.set(key, value);
            }
            // do redis opt by instance
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
    }
    public static synchronized String getData(String key)
    {
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;
        String data = null;
        try {

            cacheClient = pool.getResource();
            cacheClient.select(GlobalOption.redisDB);

            data = cacheClient.get(key);
            // do redis opt by instance
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
        return data;
    }
}
