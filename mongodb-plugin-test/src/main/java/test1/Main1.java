package test1;

import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.kit.MongoQuery;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.mongodb.MongoClient;

import java.util.List;

/**
 * Created by yek on 2017-4-7.
 */
public class Main1 {
    public static void main(String[] args) {
        MongoPlugin plugin = new MongoPlugin();
//        plugin.add("192.168.13.200",27017);
        plugin.add("192.168.13.72",27017);
        plugin.setDatabase("bidchance_data");
        MongoClient client = plugin.getMongoClient();
        MongoKit.INSTANCE.init(client, plugin.getDatabase());

        MongoQuery query=new MongoQuery();
        query.use("www_bidchance_com");
        List list = query.limit(20).descending("collect_time").find();
        for (Object o : list)
            System.out.println(o);

        client.close();
    }
}
