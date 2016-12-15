package insertTest;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.sf.json.JSONArray;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yek on 2016-8-1.
 */
public class InsertTest {
    @Test
    public void test1() {
        // 连接到 mongodb 服务
        MongoClient client = new MongoClient("localhost");
        // 连接到数据库
        MongoDatabase mongoDatabase = client.getDatabase("test");
        System.out.println("Connect to database successfully");
        // 获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("person");
        System.out.println("集合 person 选择成功");

        // insert
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("name","lisi");
        Document document = new Document();
        List<String> list = new ArrayList<String>();
        list.add("good1");
        list.add("good2");
        list.add("good3");
        JSONArray jsonArray = JSONArray.fromObject(list);
        map.put("good",jsonArray);
        document.putAll(map);

        collection.insertOne(document);
        System.out.println("插入成功！");
        client.close();
    }
}
