package test2;

import com.alibaba.fastjson.JSONObject;
import com.cybermkd.mongo.kit.MongoKit;
import com.cybermkd.mongo.kit.MongoQuery;
import com.cybermkd.mongo.kit.page.MongoPaginate;
import com.cybermkd.mongo.plugin.MongoPlugin;
import com.mongodb.MongoClient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yek on 2017-4-24.
 */
public class Main2 {

    public static void main(String[] args) {
        MongoPlugin plugin = new MongoPlugin();
//        plugin.add("192.168.13.200",27017);
        plugin.add("192.168.13.72", 27017);
        plugin.setDatabase("bidchance_data");
        MongoClient client = plugin.getMongoClient();
        MongoKit.INSTANCE.init(client, plugin.getDatabase());

        MongoQuery query = new MongoQuery();
        query.use("www_bidchance_com");
        long countAll = query.count();
        int page = 1;
        int totalPage = (int) (countAll / 1000 + 1);
        for (; page <= totalPage; page++) {
            MongoPaginate paginate = new MongoPaginate(query, 1000, page);
            List<JSONObject> list = paginate.findAll().getList();
            for (JSONObject json : list) {

                if (!json.containsKey("tag")) {
                    System.out.println("befor: " + json);
                    Date collect_time = json.getDate("collect_time");
//                System.out.println(collect_time);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(collect_time);
                    calendar.add(Calendar.YEAR, -10);

                    json.remove("collect_time");
                    json.put("collect_time", new java.sql.Date(calendar.getTime().getTime()));
                    System.out.println("after: " + json);
                    new MongoQuery().use("www_bidchance_com").byId((String) json.get("_id")).replace(json);
//                System.out.println((Date)json.getDate("collect_time"));

                }

            }
        }

        client.close();

    }
}
