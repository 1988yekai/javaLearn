package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/5/20.
 */
public class MesChannelManager {

    public String allocateChannel(String userId){
        //todo: 为用户分配一个随机字符串
        String channelId = "";
        return userId + "/c/" + channelId;
    }

    public void closeChannel(String channelId){
        //todo:释放channelId字符串对应的key。
    }

    public void suspendChannel(String channelId){
        //todo:暂停使用channel，标记为暂停状态，比如管理员暂停用户通道，或者通道数据量超过最大保存数据量
    }

    public void resumeChannel(String channelId) {
        //todo：继续使用channel，标记改为正常状态
    }

    public void updateChannel(MesChannel channel){}
}
