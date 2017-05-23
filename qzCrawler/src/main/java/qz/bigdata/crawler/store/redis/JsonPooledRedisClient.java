package qz.bigdata.crawler.store.redis;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by fys on 2015/4/16.
 */
public class JsonPooledRedisClient {
    private static JsonPooledRedisClient instance = null;
    private static final Logger logger = Logger.getLogger(JsonPooledRedisClient.class);

    private PooledRedisClient jedisClient = null;

//    private JedisPool pool = null;
//    private String password = GlobalOption.redisPassword;
//    private int dbid = 0;

    //    static{
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(60);
//        config.setMaxIdle(20+1);
//        config.setMaxWait(20000l);
//        //pool = new JedisPool(config,GlobalOption.redisIP, GlobalOption.redisPort);
//    }
    private JsonPooledRedisClient(){

    }
    private JsonPooledRedisClient(String redisIP, int redisPort, String redisPassword){
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(60);
//        config.setMaxIdle(20 + 1);
//        config.setMaxWait(20000l);
//        this.pool = new JedisPool(config, redisIP, redisPort);
//        this.password = redisPassword;
//        this.dbid = 0;
        jedisClient = new PooledRedisClient(redisIP, redisPort, redisPassword);
    }
    private JsonPooledRedisClient(String redisIP, int redisPort, String redisPassword, boolean useLongConnection){
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxActive(60);
//        config.setMaxIdle(20 + 1);
//        config.setMaxWait(20000l);
//        this.pool = new JedisPool(config, redisIP, redisPort);
//        this.password = redisPassword;
//        this.dbid = 0;
        jedisClient = new PooledRedisClient(redisIP, redisPort, redisPassword, useLongConnection);
    }

    public synchronized static JsonPooledRedisClient getJsonPooledRedisClient(String redisIP, int redisPort, String redisPassword){
        if(instance == null) {
            instance = new JsonPooledRedisClient(redisIP, redisPort, redisPassword);
        }
        return instance;
    }

    public synchronized static JsonPooledRedisClient getJsonPooledRedisClient(String redisIP, int redisPort, String redisPassword, boolean useLongConnection){
        if(instance == null) {
            instance = new JsonPooledRedisClient(redisIP, redisPort, redisPassword, useLongConnection);
        }
        return instance;
    }
    public void selectDatabase(int dbid){
        jedisClient.getJedis().select(dbid);
    }

    public boolean exist(String key) throws Exception{
        try {
            return jedisClient.exist(key);
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public <T> T get(String key, Class<T> valueClass){
        String value = jedisClient.get(key);
        if(value == null) return null;
        return JSON.parseObject(value, valueClass);
    }
    public <T> List<T> getArray(String key, Class<T> valueClass){
        String value = jedisClient.get(key);
        if(value == null) return null;
        return JSON.parseArray(value, valueClass);
    }
    public boolean set(String key, Object instance){
        String value = JSON.toJSONString(instance);
        return jedisClient.set(key, value);
    }

    public boolean hset(String key, String field, Object instance){
        String value = JSON.toJSONString(instance);
        return jedisClient.hset(key, field, value);
    }
    public <T> T hget(String key, String field, Class<T> valueClass){
        String value = jedisClient.hget(key, field);
        if(value == null) return null;
        return JSON.parseObject(value, valueClass);
    }
    public <T> List<T> hgetArray(String key, String field, Class<T> valueClass){
        String value = jedisClient.hget(key, field);
        if(value == null) return null;
        return JSON.parseArray(value, valueClass);
    }

    public <T> Map<String, T> hgetAll(String key, Class<T> valueClass){
        Map<String, T> resMap = new HashMap<String, T>();
        Map<String, String> maps = jedisClient.hgetAll(key);
        if(maps == null) return null;
        Iterator<Map.Entry<String, String>> iter = maps.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            String k = entry.getKey();
            String v = entry.getValue();
            T data = JSON.parseObject(v, valueClass);//JSONObject.toBean(JSONObject.fromObject(v), valueClass);//
            resMap.put(k, data);
        }
        return resMap;
    }

    public <T> Map<String, List<T>> hgetAllArray(String key, Class<T>  valueClass){
        Map<String, List<T>> resMap = new HashMap<String, List<T>>();
        Map<String, String> maps = jedisClient.hgetAll(key);
        if(maps == null) return null;
        Iterator<Map.Entry<String, String>> iter = maps.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            String k = entry.getKey();
            String v = entry.getValue();
            List<T> datas = JSON.parseArray(v, valueClass);//JSONObject.toBean(JSONObject.fromObject(v), valueClass);//
            resMap.put(k, datas);
        }
        return resMap;
    }

    public boolean push(String key, Object instance) throws Exception{
        String value = JSON.toJSONString(instance);
        return jedisClient.push(key, value);
    }

    public <T> T pop(String key, Class<T> valueClass) throws Exception{
        String value = jedisClient.pop(key);
        return JSON.parseObject(value, valueClass);
    }
    public <T> T pop(Class<T> valueClass, int timeout, String... keys) throws Exception {
        List<String> values = jedisClient.bpop(timeout, keys);
        if(values != null && values.size() > 1) {
            return JSON.parseObject(values.get(1), valueClass);
        }
        return null;
    }
    public <T> List<T> popArray(String key, Class<T>  valueClass) throws Exception{
        String value = jedisClient.pop(key);
        return JSON.parseArray(value, valueClass);
    }
    public <T> T popPriority(String key, Class<T> valueClass) throws Exception {
        String value = jedisClient.pop(key);
        return JSON.parseObject(value, valueClass);
    }
    public <T> T bpop(String key, Class<T> valueClass) throws Exception {
        String value = jedisClient.bpop(key);
        return JSON.parseObject(value, valueClass);
    }
    public <T> List<T> bpopArray(String key, Class<T>  valueClass) throws Exception {
        String value = jedisClient.bpop(key);
        return JSON.parseArray(value, valueClass);
    }
    public <T> T bpop(String key, int timeout, Class<T> valueClass) throws Exception{
        String value = jedisClient.bpop(key, timeout);
        return JSON.parseObject(value, valueClass);
    }
    public <T> List<T> bpopArray(String key, int timeout, Class<T>  valueClass) throws Exception{
        String value = jedisClient.bpop(key, timeout);
        return JSON.parseArray(value, valueClass);
    }

    public boolean hexist(String key, String field) throws Exception {
        try {
            return jedisClient.hexist(key, field);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean hdelete(String key, String field) throws Exception {
        return jedisClient.hdel(key, field);
    }

    public long hincrease(String key, String field, long value) throws Exception {
        try {
            return jedisClient.hincrease(key, field, value);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<String> getAllList(String keys) throws Exception {
        List<String> values = jedisClient.getAll(keys);
        return values;
    }

    //有序集合 操作
    public boolean redisZadd(String key, String score,String values) throws Exception {
        String result = JSON.toJSONString(values);
        double tmp = Double.parseDouble(score);
        return jedisClient.zadd(key,tmp,result);
    }
    public Set<String> redisZrangeByScore(String key, String min,String max) throws Exception {
        return jedisClient.zrangeByScore(key, min, max);
    }
    public boolean redisZremRangeByScore(String key, String min,String max) throws Exception {
        return jedisClient.zremRangeByScore(key,min,max);
    }
    public boolean redisZremByValue(String key, String value) throws Exception {
        return jedisClient.zremByValue(key,value);
    }

    public Long redisGetQueueLlen(String key) {
        return jedisClient.getLlen(key);
    }

    public Long redisGetSetHlen(String key) {
        return jedisClient.getHlen(key);
    }

    public PooledRedisClient getJedisClient(){
        return jedisClient;
    }
}
