package qz.bigdata.crawler.store.redis;

/**
 * Created by fys on 2015/5/20.
 * 用户Key设计
 * User: u/[id]
 * User queue: u/[id]
 * User settings: u/[id]/settings
 * User info: u/[id]/info
 * User friends: u/[id]/friends [user id] value
 * User 关注: u/[id]/focus [user id] value
 * User 粉丝: u/[id]/fans [user id] value
 * User 微博: u/[id]/pu
 * User token : u/[id]/token
 * User password : u/[id]/password
 * User channel : u/[id]/c/[channel id]
 * User channel settings : u/[id]/c/[channel id]/settings
 *
 * 用户组Key设计
 * Group: g/[id]
 * Group queue: g/[id]
 * Group settings: g/[id]/settings
 * Group info: g/[id]/info
 * Group members: g/[id]/members [user id] role
 * Group token : g/[id]/token
 * Group password : g/[id]/password
 *
 * 用户管理Key设计
 * User set : mg/u [user id] whatToStore? user name?
 * Group set : mg/g [group id] ? group name?
 * Channel set : mg/c [channel id] 通道由用户自己管理还是统一管理？
 */
public class MesUserManager {

    public String allocateGroup(){return null;}

    public void closeGroup(String groupId){}

    public String[] getGroupMembers(String groupId){return null;}

    public boolean addUserToGroup(String groupId, String userId){return true;}

    public void removeUserFromGroup(String groupId, String userId){}

    //
    public String allocateUser(){ return null;}


    public void closeUser(String userId){
        //todo:释放所有跟user相关的key。
    }

    public void suspendUser(String user) {
        //todo：暂停用户，用户的通道不允许继续接受消息
    }

    public void resumeUser(String user){
        //todo：将用户改为正常状态，用户的通道允许继续接受消息
    }

    public void updateMesUser(MesUser user){}

    public void updateMesGroup(MesGroup group){}

}
