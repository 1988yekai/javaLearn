package qz.bigdata.crawler.core;

import java.util.List;
import qz.bigdata.crawler.browser.*;
/**
 * Created by fys on 2015/1/26.
 */
public class GenericPageHandler extends PageHandler {
    @Override
    public IPageHandler cloneInstance(SessionHandler sessionHandler) {
        return null;
    }

    protected void onInit() {
        return;
    }

    @Override
    public void setSessionHandler(SessionHandler sessionHandler)
    {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void setUrlInfo(UrlInfo urlInfo) {
        this.urlInfo = urlInfo;
    }

    @Override
    public Boolean isMatch(UrlInfo url) {
        return false;
    }

    @Override
    public List<List<UrlInfo>> getPublicLinks(Browser browser) {
        return null;
    }

    @Override
    public List<UrlInfo> getPrivateLinks(Browser browser) {
        return null;
    }

    @Override
    public void beforeLoadPage(Browser browser) {

    }

    @Override
    public void afterLoadPage(Browser browser) {

    }

    @Override
    public boolean savePageData(Browser browser) {
        return false;
    }
}
