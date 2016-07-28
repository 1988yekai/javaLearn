package setTest;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016-6-20.
 */
public class SetTest1 {
    public static void main(String[] args) {
        Set set1 = new HashSet();
        String s1 = "hello ";
        String s2 =s1;
        String s3 = "world!";
        set1.add(s1);
        boolean flag = set1.add(s2);
        set1.add(s3);
        System.out.println(set1.size());
        System.out.println(set1);
        System.out.println(flag);
    }
}
