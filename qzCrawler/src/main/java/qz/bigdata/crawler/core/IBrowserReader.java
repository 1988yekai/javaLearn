package qz.bigdata.crawler.core;

import java.util.List;

/**
 * Created by fys on 2015/1/8.
 */
public interface IBrowserReader {
    String getEleText(String locateType, String locateId);
    List<String> getEleTexts(String locateType, String locateId);
    String getEleAttribute(String locateType, String locateId, String attributeName);
    List<String> getEleAttributes(String locateType, String locateId, String attributeName);
    String getUrl();
    String getTitle();
    String getPage();
    List<String> getWindowTitles();
}
