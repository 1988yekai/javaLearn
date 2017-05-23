package qz.bigdata.crawler.core;

/**
 * Created by fys on 2015/1/11.
 */
public interface IBrowserManager {

    Boolean registerBrowserController(BrowserController wpc) throws Exception;
    Boolean updateBrowserController(BrowserController wpc) throws Exception;
    Boolean unregisterBrowserController(String name) throws Exception;
    int count();
}
