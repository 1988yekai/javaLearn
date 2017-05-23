package qz.bigdata.crawler.core;

import java.util.List;
import qz.bigdata.crawler.store.redis.UrlCache;
import qz.bigdata.crawler.browser.Browser;

/**
 * Created by fys on 2015/1/15.
 */
public interface IPageHandler {
    Boolean isMatch(UrlInfo url);
    List<List<UrlInfo>> getPublicLinks(Browser browser);
    List<UrlInfo> getPrivateLinks(Browser browser);
    Boolean needVisit(UrlCache url);//类型应该为其他类型，因为Redis数据库支持的存储类型有限
    void beforeLoadPage(Browser browser);
    void afterLoadPage(Browser browser);
    boolean savePageData(Browser browser);
    void setSessionHandler(SessionHandler sessionHandler);
    void setUrlInfo(UrlInfo bu);
    void setController(BrowserController controller);
    IPageHandler cloneInstance(SessionHandler sessionHandler);
    void registerDataModel(Class model);
}
