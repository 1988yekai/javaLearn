package readTypeTest;

import org.bson.BsonDocument;

/**
 * Created by yek on 2016-7-27.
 */
public class TypeUtils {

    static String getType(BsonDocument bsonDocument, String key) {
        if (bsonDocument.isDateTime(key)){
            return "DateTime";
        } else if (bsonDocument.isString(key)) {
            return "String";
        } else if (bsonDocument.isDocument(key)) {
            return "Document";
        } else if (bsonDocument.isArray(key)) {
            return "Array";
        } else if (bsonDocument.isBinary(key)) {
            return "Binary";
        } else if (bsonDocument.isBoolean(key)) {
            return "Boolean";
        } else if (bsonDocument.isDouble(key)) {
            return "Double";
        } else if (bsonDocument.isInt32(key)) {
            return "Int32";
        } else if (bsonDocument.isInt64(key)) {
            return "Int64";
        } else if (bsonDocument.isNull(key)) {
            return "Null";
        } else if (bsonDocument.isObjectId(key)) {
            return "ObjectId";
        } else if (bsonDocument.isTimestamp(key)) {
            return "Timestamp";
        }

        return null;
    }
}
