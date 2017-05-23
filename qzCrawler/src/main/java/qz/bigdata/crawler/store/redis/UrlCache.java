package qz.bigdata.crawler.store.redis;

import com.ibm.icu.text.SimpleDateFormat;
import qz.bigdata.crawler.core.UrlInfo;


import java.util.Date;
import java.util.List;

/**
 * Created by Jiest on 2015/1/27.
 */
public class UrlCache {
    public static final int urlCacheValueLength = 4;
    public String url = "";
    public String parentUrl = "";
    public String visitedTime = "";
    public String extraInfo = "";

    public  UrlCache(UrlInfo ui, String extraInfo)
    {
        Date date=new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.url = ui.url.toString();
        if (ui.parentUrl!=null)
        {
            this.parentUrl = ui.parentUrl.toString();
        }
        this.visitedTime = df.format(date);
        this.extraInfo=extraInfo;
    }
    public UrlCache(List<String> urlCacheValue)
    {
        if(urlCacheValue != null && urlCacheValue.size() >= UrlCache.urlCacheValueLength)
        {
            this.url = urlCacheValue.get(0);
            this.parentUrl = urlCacheValue.get(1);
            this.visitedTime = urlCacheValue.get(2);
            this.extraInfo = urlCacheValue.get(3);
        }
    }
}
