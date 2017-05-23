package regexTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yek on 2017-3-6.
 */
public class Test1 {
    public static void main(String[] args) {
        String string ="http://www.legaldaily.com.cn/index_article/content/2017-03/12/content_7049092.htm?node=5955";

        Pattern pattern = Pattern.compile("/content/([\\d]{4}-[\\d]{2}/[\\d]{2})/");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()){
            System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
        }
    }
}
