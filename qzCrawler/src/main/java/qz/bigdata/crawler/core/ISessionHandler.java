package qz.bigdata.crawler.core;

import qz.bigdata.crawler.browser.Browser;

/**
 * Created by fys on 2015/1/19.
 */
public interface ISessionHandler {
    void beforeStartSession(Browser browser);
    void afterEndSession(Browser browser);
    void saveSessionData(Browser browser);
}
