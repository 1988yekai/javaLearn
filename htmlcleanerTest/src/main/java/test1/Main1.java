package test1;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

/**
 * Created by yek on 2017-2-20.
 */
public class Main1 {
    public static void main(String[] args) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("testData/3.html"), "gbk"))) {
            String data = null;
            while ((data = bufferedReader.readLine()) != null)
                content.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(content);

        String tag = "";
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode originalNode = cleaner.clean(content.toString());

        Object[] as = null;
//        TagNode divNode =riginalNode.findElementByAttValue("class","xllabel-l",true,false);
        try {
            as = originalNode.evaluateXPath("//div[@class='xllabel-l']/a");
        } catch (XPatherException e) {
            System.out.println("标签为空！");
        }
        if (as != null) {
            for (Object a : as) {
                tag += ((TagNode) a).getText() + " ";
            }
        }
        tag = tag.trim().replaceAll(" ", ",");
        System.out.println(tag);

        // table_content
        Map<String,String> tabMap= new LinkedHashMap<>();
        Object[] trs = null;
        try {
            trs = originalNode.evaluateXPath("//table[@id=\"infotablecontent\"]/tbody/tr");// //*[@id="infotablecontent"]
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        if (trs != null) {
            for (Object tr : trs) {
                int i = 0;
                String key = null;
                String val = null;
                for (TagNode tagNode : ((TagNode) tr).getChildTags()) {
                    if ((i++) % 2 == 0){
                        key = tagNode.getText().toString().trim();
                    } else {
                        val = tagNode.getText().toString().trim();
                        if (key != null && !"".equals(key))
                            tabMap.put(key,val);
                    }
                }

            }
        }
        System.out.println(tabMap);

        //article_content
        String article = "";
        TagNode dd = originalNode.findElementByAttValue("id","infohtmlcon",true,true);
        article = dd.getText().toString().trim();
//        article = article.replaceAll("   ","\n").replaceAll("  ","\n");
        article = article.replaceAll("&nbsp;","").replaceAll("\\\\n\\\\n\\\\n","\n").replaceAll("\\\\n\\\\n","").replaceAll("\\\\n \\\\n","").replaceFirst("\\\\n","").trim();
//        article = article.replaceAll("\n\n\n", "").replaceAll("\n\n", "").trim();
        System.out.println(article);

    }
}
