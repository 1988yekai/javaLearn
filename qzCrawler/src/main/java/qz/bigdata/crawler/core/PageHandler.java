package qz.bigdata.crawler.core;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.store.redis.UrlCache;
import qz.bigdata.crawler.browser.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by fys on 2015/1/16.
 */
public abstract class PageHandler implements IPageHandler {
    protected UrlInfo urlInfo;
    protected BrowserController controller;
    protected SessionHandler sessionHandler;
    protected List<Class> dataModels;
    protected Logger logger = Logger.getLogger(getClass());
    protected PageHandler( )
    {
        this.controller = null;
        this.sessionHandler = null;
        this.onInit();
    }
    protected PageHandler(SessionHandler sessionHandler)
    {
        this.sessionHandler = sessionHandler;
        this.onInit();
    }
    public void setController(BrowserController controller){
        this.controller = controller;
    }
    public void registerDataModel(Class model)
    {
        if(!this.dataModels.contains(model))
        {
            this.dataModels.add(model);
        }
    }
    protected void onInit() {
        this.dataModels = new LinkedList<Class>();
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
        logger.debug("beforeLoadPage: " + urlInfo.url.toString());
    }

    @Override
    public void afterLoadPage(Browser browser) {
        logger.debug("afterLoadPage: " + urlInfo.url.toString());
    }

    @Override
    public boolean savePageData(Browser browser) {
        logger.debug("savePageData: " + urlInfo.url.toString());
        //1遍历所有data model
        //2取出一个并生成对象
        //3从配置文件初始化
        //4调用方法根据配置文件依次初始化其基本变量
        //5对于类型变量，重复2-4的过程，直到
        //6保存对象
        return false;
    }

    @Override
    public abstract IPageHandler cloneInstance(SessionHandler sessionHandler);

    @Override
    public Boolean needVisit(UrlCache url) {
        return false;
    }
}
