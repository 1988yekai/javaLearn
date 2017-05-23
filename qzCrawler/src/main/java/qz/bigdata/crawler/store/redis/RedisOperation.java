package qz.bigdata.crawler.store.redis;

import net.sf.json.JSONObject;
import qz.bigdata.crawler.configuration.GlobalOption;
import org.apache.log4j.Logger;
import qz.bigdata.crawler.core.IUrlQueue;
import qz.bigdata.crawler.core.UrlInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;
//import us.codecraft.webmagic.selenium.core.UrlInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *  redis 数据库的连接
 *  增、删、查等操作
 */
public class RedisOperation implements IUrlQueue {
	private static final Logger logger = Logger.getLogger(RedisOperation.class);
	private static JedisPool pool = null;
//    private static String  urlQueue = GlobalOption.redisUrlQueueString;
	private static String  password = GlobalOption.redisPassword;
	 static{
	        JedisPoolConfig config = new JedisPoolConfig();
	        config.setMaxActive(60);
	        config.setMaxIdle(20+1);
	        config.setMaxWait(20000l);
	        pool = new JedisPool(config,GlobalOption.redisIP, GlobalOption.redisPort);
	    }


    @Override
    public UrlInfo popUrlInfo(){
        Jedis cacheClient = null;
        boolean borrowOrOprSuccess = true;
        try {
            cacheClient = getJedis();
//            if(cacheClient.exists(GlobalOption.redisUrlQueueString))
            String jsonUrlInfo = cacheClient.rpop(GlobalOption.redisUrlQueueString);
            return (UrlInfo) JSONObject.toBean(JSONObject.fromObject(jsonUrlInfo), UrlInfo.class);
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
        return null;
    }
    //将要爬取的url插入队列
    @Override
    public void pushUrlInfo(UrlInfo urlInfo){
        Jedis cacheClient = null;
        boolean borrowOrOprSuccess = true;
        try {
            cacheClient = getJedis();
//            if(cacheClient.exists(GlobalOption.redisUrlQueueString))
            String jsonUrlInfo = JSONObject.fromObject(urlInfo).toString();
            cacheClient.lpush(GlobalOption.redisUrlQueueString, jsonUrlInfo);
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

    /** 查看是否存在相同keys */
    public static  Boolean redisKeyExists(String key){
        boolean isExisted = true;
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;
        try {
            cacheClient = getJedis();
            isExisted =  cacheClient.exists(key);
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
        return isExisted;
    }

     /** 设置 keys 过期时间 */
     public static  boolean redisExpire(String key,int time){
         boolean borrowOrOprSuccess = true;
         Jedis cacheClient = null;
         try {
             cacheClient = getJedis();
             cacheClient.expire(key, time);
         } catch (JedisConnectionException e) {
             logger.warn(e.getMessage());
             borrowOrOprSuccess = false;
             if (cacheClient != null)
                 pool.returnBrokenResource(cacheClient);
         } finally {
             if (borrowOrOprSuccess)
                 pool.returnResource(cacheClient);
         }
         return borrowOrOprSuccess;
     }

    /** 删除 Redis 库中的keys */
    public static  boolean redisDel(String key){
    	boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;
        try {
            cacheClient = getJedis();
            cacheClient.del(key);
        } catch (JedisConnectionException e) {
        	logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);
        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
    	return borrowOrOprSuccess;
    }

    /** 插入 Redis 库 */
    public static void set(String key, List<String> valueList){
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;
        try {
            cacheClient = getJedis();
//            cacheClient.lpush(key,valueList.get(0),valueList.get(1),valueList.get(2),valueList.get(3));
            for (String urlInfo:valueList)
            {
                cacheClient.rpush(key, urlInfo);
            }
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

    /** 查询 key 的values */
    public static List<String> get(String key){
        boolean borrowOrOprSuccess = true;
        Jedis cacheClient = null;
        List<String> value = new ArrayList<String>();
        try {
            cacheClient = getJedis();
            value = cacheClient.lrange(key, 0, -1);
        } catch (JedisConnectionException e) {
            logger.warn(e.getMessage());
            borrowOrOprSuccess = false;
            if (cacheClient != null)
                pool.returnBrokenResource(cacheClient);

        } finally {
            if (borrowOrOprSuccess)
                pool.returnResource(cacheClient);
        }
        return value;
    }

    public static Jedis getJedis(){
        Jedis cacheClient = pool.getResource();
        if( !password.equals("")){
            cacheClient.auth(password);
        }
        RedisDBManager rdbm = new RedisDBManager();
        cacheClient.select( rdbm.getProjectId( GlobalOption.redisDbName));
        return cacheClient;
    }
}
