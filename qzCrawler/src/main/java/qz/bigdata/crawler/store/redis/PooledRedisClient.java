package qz.bigdata.crawler.store.redis;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.configuration.GlobalOption;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * Created by fys on 2015/5/1.
 */
public class PooledRedisClient {

    private static PooledRedisClient instance = null;

    private static final Logger logger = Logger.getLogger(JsonPooledRedisClient.class);
    private JedisPool pool = null;
    private boolean useLongConnection = false;
    private Jedis jedis = null;
    private String password = GlobalOption.redisPassword;
    private int dbid = 0;

    //    static{
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(60);
//        config.setMaxIdle(20+1);
//        config.setMaxWait(20000l);
//        //pool = new JedisPool(config,GlobalOption.redisIP, GlobalOption.redisPort);
//    }
    private PooledRedisClient(){

    }
    public PooledRedisClient(JedisPoolConfig config, String redisIP, int redisPort, String redisPassword){
//        this.pool = new JedisPool(config, redisIP, redisPort);
        this.pool = new JedisPool(config, redisIP, redisPort, 5*60*1000);
        this.password = redisPassword;
        this.dbid = 0;
    }
    public PooledRedisClient(String redisIP, int redisPort, String redisPassword){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(60000);
        config.setMaxIdle(5000 + 1);
        config.setMaxWait(20000l);
//        this.pool = new JedisPool(config, redisIP, redisPort);
        this.pool = new JedisPool(config, redisIP, redisPort, 5*60*1000);
        this.password = redisPassword;
        this.dbid = 0;
    }
    public PooledRedisClient(String redisIP, int redisPort, String redisPassword, boolean longConnection){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(60000);
        config.setMaxIdle(5000 + 1);
        config.setMaxWait(20000l);
//        this.pool = new JedisPool(config, redisIP, redisPort);
        this.pool = new JedisPool(config, redisIP, redisPort, 5*60*1000);
        this.password = redisPassword;
        this.dbid = 0;
        this.useLongConnection = longConnection;
    }

    public synchronized static PooledRedisClient getPooledRedisClient(String redisIP, int redisPort, String redisPassword){
        if(instance == null) {
            instance = new PooledRedisClient(redisIP, redisPort, redisPassword);
        }
        return instance;
    }
    public synchronized static PooledRedisClient getPooledRedisClientOfLongConnection(String redisIP, int redisPort, String redisPassword){
        if(instance == null) {
            instance = new PooledRedisClient(redisIP, redisPort, redisPassword, true);
        }
        return instance;
    }
    public boolean getUseLongConnection(){
        return this.useLongConnection;
    }

    public void selectDatabase(int dbid){
        Jedis jedis = null;
        try{
            jedis = this.getJedis();
            jedis.select(dbid);
        }
        catch (Exception ex){
            pool.returnBrokenResource(jedis);
        }
        finally {
            if(jedis != null)
                pool.returnResource(jedis);
        }
    }

    public void releaseJedis(Jedis jedis){
        pool.returnResource(jedis);
    }

    public Jedis getJedis(){
        if(this.useLongConnection && this.jedis != null){
            return this.jedis;
        }
        Jedis jedisClient;
        try {
            jedisClient = pool.getResource();
            if(this.useLongConnection){
                this.jedis = jedisClient;
            }
        }
        catch (JedisConnectionException jex){
            return null;
        }
        try{
            if(!password.equals("")){
                jedisClient.auth(password);
            }
            return jedisClient;
        }
        catch (Exception e){
            pool.returnBrokenResource(jedisClient);
            pool.returnResource(jedisClient);
            if(this.useLongConnection){
                this.jedis = null;
            }
            return null;
        }

        //RedisDBManager rdbm = new RedisDBManager(cacheClient);
        //cacheClient.select( 0 );// rdbm.getProjectId( GlobalOption.redisDbName));
    }
    public void auth(Jedis jedis){
        if( !password.equals("")){
            jedis.auth(password);
        }
    }
    public JedisPool getPool(){
        return this.pool;
    }

    public boolean exist(String key) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.exists(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        //return false or throw new Exception?
    }

    public boolean delete(String key)throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.del(key) > 0 ? true : false;
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

    public long decr(String key) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.decr(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }
    public long incr(String key) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            return cacheClient.incr(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

    //    public Object get(String key, Class valueClass){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        try {
//            cacheClient = getJedis();
//            String value = cacheClient.get(key);
//
//
//            return JSON.parseObject(value, valueClass);//JSONObject.toBean(JSONObject.fromObject(value), valueClass);//
//
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//        return null;
//    }
    public String get(String key){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.get(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return null;
    }

    public boolean set(String key, String value){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            cacheClient.set(key, value);
            return true;
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return false;
    }

    public boolean hset(String key, String field, String value){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            cacheClient.hset(key, field, value);
            return true;
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return false;
    }
    public String hget(String key, String field){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.hget(key, field);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return null;
    }

    public boolean hdel(String key, String field) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.hdel(key, field) > 0l ? true : false;
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

    public Map<String, String> hgetAll(String key){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.hgetAll(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return null;
    }

    public boolean push(String key, String value) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            cacheClient.lpush(key, value);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return true;
    }

    //    public Object pop(String key, Class valueClass){
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        try {
//            cacheClient = getJedis();
//            String value = cacheClient.rpop(key);
//            return JSON.parseObject(value, valueClass);//JSONObject.toBean(JSONObject.fromObject(value), valueClass);//
//
//        } catch (JedisConnectionException e) {
//            logger.warn(e.getMessage());
//            borrowOrOprSuccess = false;
//            if (cacheClient != null)
//                pool.returnBrokenResource(cacheClient);
//            //todo:应该抛出异常
//        } finally {
//            if (borrowOrOprSuccess)
//                pool.returnResource(cacheClient);
//        }
//        return null;
//    }
    public String pop(String key) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.rpop(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }
    //    public Object bpop(String key, Class valueClass) {
//        Jedis cacheClient = null;
//        boolean borrowOrOprSuccess = true;
//        try {
//            cacheClient = getJedis();
//            List<String> values = cacheClient.brpop(0, new String[]{key});
//            if(values != null && values.size() >= 2) {
//                return JSON.parseObject(values.get(1), valueClass);//JSONObject.toBean(JSONObject.fromObject(values.get(1)), valueClass);//
//            }
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
//        return null;
//    }
    public String bpop(String key) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            List<String> values = cacheClient.brpop(0, new String[]{key});
            if(values != null && values.size() >= 2) {
                return values.get(1);
            }
            throw new Exception("return value is null or length < 2.");
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }
    public String bpop(String key, int timeout) throws Exception {
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            List<String> values = cacheClient.brpop(timeout, key);
            if(values == null){
                throw new TimeoutException("timeout to pop a cell for key: " + key);
            }
            if(values.size() < 2) {
                throw new Exception("return value is null or length < 2.");
            }
            return values.get(1);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }
    public List<String> bpop(int timeout, String... keys) throws Exception {
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.brpop(timeout, keys);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

    public static void main(String[] args){

        final String lockKey = "\\queue\\test\\pooled";
        final PooledRedisClient client = PooledRedisClient.getPooledRedisClient(
                GlobalOption.redisIP, GlobalOption.redisPort, GlobalOption.redisPassword
        );
//        final String lockKey = locker.prepareLock(1);
//        if(lockKey == null) return;
        int count = 0;
        System.out.println(lockKey + " : " + new Date());
        if(true) {
            for (int i = 0; i < 1000; i++) {
                Thread t = new Thread() {
                    public void run() {
                        try {
                            while(true) {
                                String value = client.bpop(lockKey);
                                System.out.println("bpop value: " + value);
                            }
                        }
                        catch (Exception ex){

                        }
                    }
                };
                t.start();
            }
        }
    }

    public boolean hexist(String key,String filed) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.hexists(key, filed);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

    public List<String> getAll(String key){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.lrange(key, 0, -1);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return null;
    }
    //有序集合操作
    public boolean zadd(String key, Double score,String value) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            cacheClient.zadd(key,score,value);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return true;
    }
    //返回有一部分
    public Set<String> zrangeByScore(String key, String min,String max) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            return cacheClient.zrangeByScore(key,min,max);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }
    //删除某一部分
    public boolean zremRangeByScore(String key, String min,String max) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            cacheClient.zremrangeByScore(key, min, max);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return true;
    }
    public boolean zremByValue(String key, String value) throws Exception{
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            cacheClient.zrem(key, value);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return true;
    }

    public long getLlen(String key){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.llen(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return -1;
    }

    public long getHlen(String key){
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }

            return cacheClient.hlen(key);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
        return -1;
    }

    public long hincrease(String key, String field, long value) throws Exception {
        Jedis cacheClient = null;
        try {
            if(this.useLongConnection && this.jedis != null){
                cacheClient = this.jedis;
            }
            else {
                cacheClient = pool.getResource();
                auth(cacheClient);
            }
            return cacheClient.hincrBy(key, field, value);
        }
        catch (Exception ex){
            logger.warn(ex.getMessage());
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnBrokenResource(cacheClient);
            throw ex;
        }
        finally {
            if (cacheClient != null && this.useLongConnection != true)
                pool.returnResource(cacheClient);
        }
    }

}



