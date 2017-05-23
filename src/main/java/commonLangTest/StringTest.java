package commonLangTest;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by yek on 2017-5-21.
 */
public class StringTest {
    public static void main(String[] args) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("htmlTest/xmlcode.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;
        while ((temp = bufferedReader.readLine())!=null){
            stringBuilder.append(temp).append("\n");
        }
        String page = StringEscapeUtils.unescapeXml(stringBuilder.toString());
        System.out.println(page);
    }
}
