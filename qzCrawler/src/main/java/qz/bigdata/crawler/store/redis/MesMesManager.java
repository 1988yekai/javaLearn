package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/5/20.
 */
public class MesMesManager {

    public void sendMessage(String channelId, Mes message){ }
    public void sendMessage(String[] channelIds, Mes message){ }
    public void sendData(String channelId, byte[] data){ }

    public Mes receiveMessage(String channelId){return null;}
    public byte[] receiveData(String channelId){return null;}

}
