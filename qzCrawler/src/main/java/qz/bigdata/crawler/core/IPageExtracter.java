package qz.bigdata.crawler.core;

import javafx.util.Pair;
import org.openqa.selenium.WebElement;

import java.util.List;
import qz.bigdata.crawler.browser.*;

/**
 * Created by fys on 2015/1/26.
 */
public interface IPageExtracter {

    List<String> extract(WebElement parentEle, Pair<String, List<String>> selector, Browser browser);
    List<String> extract(List<String> values);

}