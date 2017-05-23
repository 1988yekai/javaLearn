package qz.bigdata.crawler.store.redis;

import java.util.List;

/**
 * Created by fys on 2015/5/20.
 */
public class MesGroup {
    public String name;
    public String groupId;
    public String messageId;
    public String description;
    public String token;
    public String password;
    public boolean allowed;
    public List<MesUser> users;
}
