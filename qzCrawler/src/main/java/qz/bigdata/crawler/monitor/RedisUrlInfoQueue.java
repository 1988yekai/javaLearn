package qz.bigdata.crawler.monitor;

import qz.bigdata.crawler.configuration.GlobalOption;
import qz.bigdata.crawler.core.UrlInfo;
import qz.bigdata.crawler.store.redis.JsonPooledRedisClient;
import qz.bigdata.crawler.store.redis.RedisDBManager;
import qz.bigdata.crawler.store.redis.RedisDistributedObjectManager;

import java.util.List;

/**
 * Created by fys on 2015/4/21.
 */
public class RedisUrlInfoQueue {

    private static String urlInfoQueueKey = "queue_urlinfo";
    private JsonPooledRedisClient client;

    public RedisUrlInfoQueue(){
        //获得本机ip和进程id，还是生成一个uuid？
        this.client = JsonPooledRedisClient.getJsonPooledRedisClient(GlobalOption.redisIP,
                GlobalOption.redisPort,
                GlobalOption.redisPassword);
        RedisDBManager rdbm = new RedisDBManager();
        this.client.selectDatabase(rdbm.getProjectId(GlobalOption.redisDbName));

//        this.rm = new RedisDistributedObjectManager(GlobalOption.redisIP,
//                GlobalOption.redisPort,
//                GlobalOption.redisPassword);
    }

    public List<UrlInfo> pop() throws Exception{
        return this.client.popArray(RedisUrlInfoQueue.urlInfoQueueKey, UrlInfo.class);
    }
    public List<UrlInfo> bpop() throws Exception{
        return this.client.bpopArray(RedisUrlInfoQueue.urlInfoQueueKey, UrlInfo.class);
    }
    public List<UrlInfo> bpopArray(int timeout) throws Exception{
        return this.client.bpopArray(RedisUrlInfoQueue.urlInfoQueueKey, timeout, UrlInfo.class);
    }
    public void push(List<UrlInfo> urlInfos) throws Exception{
        this.client.push(RedisUrlInfoQueue.urlInfoQueueKey, urlInfos);
    }
}
