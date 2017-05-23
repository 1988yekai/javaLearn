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
public class Main3 {
    public static void main(String[] args) throws Exception{
        StringBuffer stringBuffer = new StringBuffer();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("htmlTest/3.html"))){
            String temp = null;
            while (null != (temp = bufferedReader.readLine())){
                stringBuffer.append(temp);
            }
        }
        String page = stringBuffer.toString();
        String html = StringUtils.getLastConfigValue(page,"<!\\[CDATA\\[<div>(.*?)<\\/div>\\]\\]","");
//        String html = StringUtils.getLastConfigValue(page,"<!\\[CDATA\\[<div>(.*?)<\\/div>\\]\\]","");
//        String html = StringUtils.getLastConfigValue(page,"<!\\[CDATA\\[<div>.*?((<TABLE>|<table>)(.*?)(</TABLE>)).*?<\\/div>\\]\\]","");

        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode originalNode = cleaner.clean(html);
        TagNode table = originalNode.findElementByName("table",true);
        System.out.println(table.getText().toString());
        System.out.println("========================================");
        // jsoup
        Document document = Jsoup.parse(page);
        Elements divs = document.select("div.contents div.content div.projectcontent.tenderC");

        System.out.println((Jsoup.parse(divs.last().text())).select("table").text());
    }
}
