package qz.bigdata.crawler.store.redis;

import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiest on 2015/1/27.
 */
public class RedisUrlCacheManager {
    public static Boolean exist(String urlKey) throws  Exception
    {
        return RedisOperation.redisKeyExists(urlKey);
    }
    public static void setExpire(String urlKey, int seconds) throws  Exception
    {
        RedisOperation.redisExpire(urlKey, seconds);
    }
    public static UrlCache get(String urlKey) throws  Exception
    {
        List<String> urlCacheValue = RedisOperation.get(urlKey);
        return new UrlCache(urlCacheValue);
    }
    public static void set(UrlCache urlCache) throws  Exception
    {
        RedisOperation.set(urlCache.url, RedisUrlCacheManager.toUrlCacheList(urlCache));
    }
    public static void del(String urlKey) throws  Exception
    {
        RedisOperation.redisDel(urlKey);
    }

    private static UrlCache toUrlCache( List<String> urlCacheValue)
    {
        return new UrlCache(urlCacheValue);
    }

    private static List<String> toUrlCacheList( UrlCache urlCache) throws  Exception
    {
        List<String> urlCacheValue = new ArrayList<String>();
        urlCacheValue.add(urlCache.url);
        urlCacheValue.add(urlCache.parentUrl);
        urlCacheValue.add(urlCache.visitedTime);
        urlCacheValue.add(urlCache.extraInfo);
        return urlCacheValue;
    }
}
