package qz.bigdata.crawler.core;

import javafx.util.Pair;
import org.openqa.selenium.WebElement;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import qz.bigdata.crawler.browser.*;

/**
 * Created by fys on 2015/1/20.
 */
public class PageExtracter {
    Browser browser;
    Dictionary<String, String[]> extractInfoDic;
    HashMap<String, List<String>> pageData;
    public HashMap<String, List<String>> getPageData(){return this.pageData;}
    public void extract(String page)
    {
        Enumeration<String> keys = extractInfoDic.keys();
        while(keys.hasMoreElements()) {
            String name = keys.nextElement();
            String[] values = extractInfoDic.get(name);
            pageData.put(name, this.getElementInfo(values));
        }
    }
    private List<String> getElementInfo(String[] values){
        return null;
    }
    public static List<String> extract(WebElement parentEle, Pair<String, List<String>> selector, Browser browser)
    {
        return null;

    }

    public static void Extract(Browser browser, IExtract extract)
    {
        extract.init();
        extract.extract(browser);
    }
}
