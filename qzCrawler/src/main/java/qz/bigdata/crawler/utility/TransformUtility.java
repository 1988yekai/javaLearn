package qz.bigdata.crawler.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by fys on 2015/5/7.
 */
public class TransformUtility {
    public static byte[] ObjectToByte(Object obj) {
        byte[] bytes = null;
        if(obj.getClass() == String.class){
            return ((String) obj).getBytes();
        }
        try  {
            //object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        }
        catch(Exception e){
            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }

    private static Object ByteToObject(byte[] bytes) {
        Object obj = null;
        try {
            //bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();

            bi.close();
            oi.close();
        }
        catch(Exception e) {
            System.out.println("translation"+e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

}
