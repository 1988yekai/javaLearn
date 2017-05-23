package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/5/20.
 */
public class MesChannel {
    public String channelId;
    public long maxMesSize;
    public long maxMesCount;
    public boolean suspended;
    public MesUser user; // or String userOrgroupId
}
