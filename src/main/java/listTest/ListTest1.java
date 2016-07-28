package listTest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016-6-20.
 */
public class ListTest1 {
    public static void main(String[] args) {
        List list1 = new LinkedList();
        String s1 = "hello ";
        String s2 =s1;
        String s3 = "world!";
        list1.add(s1);
        list1.add(s2);
        list1.add(s3);
        System.out.println(list1.size());
        System.out.println(list1);

    }
}
