package qz.bigdata.crawler.store.redis;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import qz.bigdata.crawler.configuration.GlobalOption;
import qz.bigdata.crawler.core.IUrlQueue;
import qz.bigdata.crawler.core.UrlInfo;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Created by fys on 2015/3/21.
 */
public class RedisUrlInfoQuque implements IUrlQueue {

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
            return (UrlInfo)JSONObject.toBean(JSONObject.fromObject(jsonUrlInfo),UrlInfo.class);
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
