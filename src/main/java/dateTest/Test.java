package dateTest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yek on 2017-3-29.
 */
public class Test {
    @org.junit.Test
    public void test1(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(format.format(new Date()));
    }
}
