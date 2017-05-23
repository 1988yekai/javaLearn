package qz.bigdata.crawler.store.redis;

import qz.bigdata.crawler.configuration.GlobalOption;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fys on 2015/3/11.
 */
public class RedisDBManager {
    private final static int metaDbId = 0;
    private final static String projectPrefix = "project_";
    private final static String redisDBMaxNum = "redisDBMaxNum";
    private JedisPool pool;
    //private static Map<String, int> dbMap = new HashMap<String, int>()

    public RedisDBManager(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxActive(60);
        config.setMaxIdle(20+1);
        config.setMaxWait(20000l);
        pool = new JedisPool(config, GlobalOption.redisIP, GlobalOption.redisPort);

    }
    //连接到元信息数据库，取出count key的值。
    private synchronized int getCurrentDbCount(){
        Jedis client = null;
        try {
            client = pool.getResource();
            int maxNum =0;
            boolean isexist =  client.exists(redisDBMaxNum);
            if(isexist){
                maxNum = Integer.parseInt(client.get(redisDBMaxNum));
            }else{
                client.set(redisDBMaxNum,"0");
            }
            return maxNum;
        } catch (JedisConnectionException e) {
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            return -1;
        } finally {
            pool.returnResource(client);
        }

    }

    //连接到元信息数据库，取出count key的值。
    private synchronized int  increaseDbCount(){
        Jedis client = null;
        try {
            client = pool.getResource();
            int maxNum = getCurrentDbCount();
            maxNum++;
            client.set(redisDBMaxNum,String.valueOf(maxNum));
            return maxNum;
        } catch (JedisConnectionException e) {
            if (client != null) {
                pool.returnBrokenResource(client);
            }
            return -1;
        } finally {
            pool.returnResource(client);
        }


    }

    //组装 查询key为 [projectPrefix]+[projectName]
    //查询元信息数据库，如果有则返回对应的值（转换为整数
    //如果没有，则访问 getCurrentDbCount()，获得count
    //increaseDbCount(), 即更新count为count+1
    //在原信息数据库中插入查询key，值为更新后的count。
    public synchronized int getProjectId(String projectName){
        int projectId = -1;
        if(projectName!=null && !"".equals(projectName)){
            String projectKey = projectPrefix+projectName;

            Jedis client = null;
            try {
                client = pool.getResource();
                boolean isexist =  client.exists(projectKey);
                if(isexist){
                    projectId = Integer.parseInt(client.get(projectKey));
                }else{
                    projectId = increaseDbCount();
                    client.set(projectKey,String.valueOf(projectId));
                }
            } catch (JedisConnectionException e) {
                if (client != null) {
                    pool.returnBrokenResource(client);
                }
                return -1;
            } finally {
                pool.returnResource(client);
            }
        }
        return projectId;
    }

}
