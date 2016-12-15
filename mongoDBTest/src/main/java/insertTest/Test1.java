//package insertTest;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.Mongo;
//import net.sf.json.JSONArray;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * Created by yek on 2016-8-1.
// */
//public class Test1 {
//    @Test
//    public void test1() throws Exception{
//        Mongo mg = new Mongo();
//        DB db = mg.getDB("test");
//        DBCollection persons = db.getCollection("person");
//        // insert
//        HashMap<String,Object> map = new HashMap<String, Object>();
//        map.put("name","lisi11");
//        BasicDBObject document = new BasicDBObject();
//        List<String> list = new ArrayList<String>();
//        list.add("good1");
//        list.add("good2");
//        list.add("good3");
//        JSONArray jsonArray = JSONArray.fromObject(list);
//        map.put("good",jsonArray);
//        document.putAll(map);
//
//        persons.insert(document);
//
//        mg.close();
//    }
//
//}
