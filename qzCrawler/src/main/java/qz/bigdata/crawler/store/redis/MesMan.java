package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/5/12.
 */
public class MesMan {
    private String mesQueueKey;

    public void sendMessage(String channelId, Mes message){ }

    public void sendToUser(String userId, Mes message){ }

    public void sendToGroup(String groupId, Mes message){ }


    public void receiveMes(String userId){

    }

    public void applyDataChannel(){

    }

    public void closeDataChannel(){

    }

    public void sendDataToChannel(String channel){

    }

    public void receiveDataFromChannel(String channel){

    }

    //克隆竞争者
    public MesMan cloneOne(){return null;}

    //克隆并发者
}
