package test1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by yek on 2017-5-8.
 */
public class JsoupMain1 {
    public static void main(String[] args) throws Exception {
//        String url = "http://www.zjzfcg.gov.cn/new/sxzbcjgs/949043.html";
//        Document doc = Jsoup.connect(url).get();
//        Elements elements = doc.select("div#news_content");
//        System.out.println(elements.first().text());

        String url = "http://www.ccgp-hubei.gov.cn/fnoticeAction!findFNoticeInfoByGgid_n.action?queryInfo.GGID=Eof0RjIy9Aei9oiU";
        Document document = Jsoup.connect(url).get();

        String content = "";
        Elements elements = document.select("div.notic_show_title");

        if (!elements.isEmpty()){
            content = elements.first().text().replaceAll("浏览次数：[\\d]+","");
            elements = document.select("div.notic_show_content");
            if (!elements.isEmpty()) {
                Elements elements1 = elements.first().children();
                for (Element element : elements1)
                    content += "\n"+element.text();
            }
        }
        System.out.println(content);
    }
}
