package htmlcleanerTest;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by yek on 2017-4-17.
 */
public class Main1 {
    public static void main(String[] args) throws Exception{
        // init
        StringBuffer stringBuffer = new StringBuffer();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("htmlTest/2.html"))){
            String temp = null;
            while (null != (temp = bufferedReader.readLine())){
                stringBuffer.append(temp);
            }
        }
        String html = stringBuffer.toString();

        String html1 = StringUtils.getConfigValue(html,"<!\\[CDATA\\[<div><!DOCTYPE.*?(<html.*?<\\/html>).*?<\\/!\\[CDATA\\[<div>","");
        System.out.println(html1);
        //htmlcleaner
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode originalNode = cleaner.clean(html1);
        TagNode table = originalNode.findElementByName("table",true);
        System.out.println(table.getText().toString());
        System.out.println("========================================");
        // jsoup
        Document document = Jsoup.parse(html1);
        Elements divs = document.select("body");

        System.out.println(divs.text());
    }
}
