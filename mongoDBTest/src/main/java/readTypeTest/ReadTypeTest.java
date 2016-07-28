package readTypeTest;

import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.*;
import org.junit.Test;

import java.util.List;

/**
 * Created by yek on 2016-7-27.
 */
public class ReadTypeTest {
    private MongoClient client;

    public void closeClient() {
        client.close();
    }

    public BsonDocument getBsonDocument() {
        BsonDocument bsonDocument = null;
        // 连接到 mongodb 服务
        client = new MongoClient("localhost");
        // 连接到数据库
        MongoDatabase mongoDatabase = client.getDatabase("local");
        System.out.println("Connect to database successfully");
        // 获取集合
        MongoCollection<Document> collection = mongoDatabase.getCollection("startup_log");
        System.out.println("集合 startup_log 选择成功");
        MongoCollection<BsonDocument> bsonDocumentMongoCollection = mongoDatabase.getCollection("startup_log", BsonDocument.class);
        MongoCursor<BsonDocument> Cursor = bsonDocumentMongoCollection.find().iterator();
        while (Cursor.hasNext()) {
            bsonDocument = Cursor.next();
            break;
        }
        return bsonDocument;
    }

    public String getInDocType(BsonDocument bsonDocument) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");

        int i = 0;
        for (String key : bsonDocument.keySet()) {
            if (i != 0) {
                stringBuilder.append(",\n");
            }
            i++;
            stringBuilder.append("\"" + key + "\":\"" + bsonDocument.get(key).getBsonType().toString());
            if (bsonDocument.get(key).getBsonType().toString().equals("DOCUMENT")) {
                stringBuilder.append("\n");
                String string1 = getInDocType((BsonDocument) bsonDocument.get(key));
                stringBuilder.append(string1);
            }

            if (bsonDocument.get(key).getBsonType().toString().equals("ARRAY")) {
                stringBuilder.append("\n" + getInArrType((BsonArray) bsonDocument.get(key)));
            }
            stringBuilder.append("\"");

        }

        stringBuilder.append("\n}");
        return stringBuilder.toString();
    }

    public String getInArrType(BsonArray bsonArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        int i = 0;

        List<BsonValue> list = bsonArray.getValues();
        for (BsonValue value : list) {
//            System.out.println(value.getBsonType());

            if (i != 0) {
                stringBuilder.append(",");
            }
            i++;
            stringBuilder.append("\"" + value.getBsonType());
            if (value.getBsonType().toString().equals("DOCUMENT")) {
                stringBuilder.append("\n");
                String string1 = getInDocType((BsonDocument) value);
                stringBuilder.append(string1);
            }
            if (value.getBsonType().toString().equals("DOCUMENT")) {
                stringBuilder.append("\n" + getInArrType((BsonArray) value));
            }

            stringBuilder.append("\"");
        }
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Test
    public void test1() {
        BsonDocument bsonDocument = getBsonDocument();
        System.out.println(bsonDocument.toString());

        String type = getInDocType(bsonDocument);
        System.out.println("-------------------------------------------------------");
        System.out.println(type);
        System.out.println("-------------------------------------------------------");
        System.out.println(type.replaceAll("\n", ""));

        closeClient();

    }


}
