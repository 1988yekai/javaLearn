package qz.bigdata.crawler.core;


import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.browser.*;
import qz.bigdata.crawler.store.ISaveData;
import qz.bigdata.crawler.store.redis.RedisUrlCacheManager;
import qz.bigdata.crawler.configuration.GlobalOption;
import qz.bigdata.crawler.store.redis.UrlCache;

/**
 * Created by fys on 2015/1/12.
 */
public class BrowserController implements Runnable {
    protected Browser browser;
    protected ISaveData dataSaver;
    protected Object data;
    protected static SessionHandler sessionHandler;
    public static List<IPageHandler> pageHandlers = new LinkedList<IPageHandler>();
    protected String crawlerName;
    protected String name;
    protected List<String> matchedStrRegex;
    protected Logger logger = Logger.getLogger(getClass());
    protected BrowseUrlQueue<UrlInfo> sessionUrlQueue;//BrowserController所启动的Browser要持续访问的url，必须是当前BrowserController对应处理的Url。
    protected static int maxAllowedJumpNumber = 20;
    //protected Browser browser;

    public BrowserController(List<UrlInfo> uis)
            throws InterruptedException
    {
        this.sessionUrlQueue = new BrowseUrlQueue<UrlInfo>();
        if(uis != null){
            for(UrlInfo ui : uis){
                this.sessionUrlQueue.put(ui);
            }
        }

        this.browser = new Browser(GlobalOption.browserType, GlobalOption.browserDriverPath);
    }

    public BrowserController(UrlInfo ui)
            throws InterruptedException
    {
        this.sessionUrlQueue = new BrowseUrlQueue<UrlInfo>();
        sessionUrlQueue.put(ui);
        this.browser = new Browser(GlobalOption.browserType, GlobalOption.browserDriverPath);
    }

    public BrowserController setDataSaver(ISaveData dataSaver){
        this.dataSaver = dataSaver;
        return this;
    }
    public Object getData( ){
        return this.data;
    }
    public BrowserController setData(Object data){
        this.data = data;
        return this;
    }

    public static void registerSessionHandler(SessionHandler sessionHandler)
    {
        BrowserController.sessionHandler = sessionHandler;
    }
    public static void registerPageHandler(IPageHandler pageHandler)
    {
        if(!pageHandlers.contains(pageHandler))
        {
            pageHandlers.add(pageHandler);
        }
    }

    public String getName()
    {
        return this.name;
    }

    public void putUrl(UrlInfo ui)
            throws InterruptedException
    {
        sessionUrlQueue.put(ui);
    }

    protected IPageHandler findPageHandler(UrlInfo ui)
    {
        IPageHandler find = null;

        if(ui.handlerName != null && ui.handlerName != "")
        {
            for(IPageHandler ph : BrowserController.pageHandlers) {
                if (ph.getClass().getSimpleName().equals(ui.handlerName)) {
                    find = ph.cloneInstance(this.sessionHandler);
                    find.setController(this);
                    return find;
                }
            }
        }
        for(IPageHandler ph : BrowserController.pageHandlers)
        {
            if(ph.isMatch(ui))
            {
                find = ph.cloneInstance(this.sessionHandler);
                find.setController(this);
                if(find==null)
                {
                    logger.error("错误：PageHandler的方法cloneInstance返回null值。");
                }
                return find;
            }
        }
        return null;
    }
    private void sleepForPageReady(String url) throws InterruptedException {
        int sleepCount = 0;
        while(!this.browser.getUrl().toLowerCase().equals(url.toLowerCase()))
        {
            //System.out.println("--------------------------------url不一致，当前为" + this.browser.getUrl().toLowerCase());
            //System.out.println("--------------------------------url不一致，应该为" + urlInfo.url.toString().toLowerCase());
            Thread.sleep(GlobalOption.visitSessionPageSleepTime);
            if(++sleepCount > GlobalOption.maxVisitSessionPageSleepCount){
                System.out.println("--------------------------------url不一致，超过最大等待次数。");
                System.out.println("--------------------------------url不一致，当前为" + this.browser.getUrl().toLowerCase());
                System.out.println("--------------------------------url不一致，应该为" + url);
                break;
            }
            if (sleepCount%20==0) {
                System.out.println("每2秒重新访问");
                this.browser.visit(url);
            }
            System.out.println("休眠次数："+sleepCount);
        }
    }

    protected void handlePage(IPageHandler pageHandler, UrlInfo urlInfo, Boolean visited, int count)
    {
        String url = urlInfo.url.toString();
        String lowerUrl = url.toLowerCase();
        try {
            pageHandler.setUrlInfo(urlInfo);
            pageHandler.beforeLoadPage(this.browser);
            //递归调用时不需要重新访问
            if(!visited) {
                //如果访问失败则重复访问直到最大次数
                int num=0;
                while(true) {
                 try {
                     this.browser.visit(url);
                     break;
                 }
                 catch (Exception e) {
                     System.out.println("--------------------------------访问页面失败：" + url);
                     logger.warn("访问页面失败：", e);
                     if(++num >= GlobalOption.maxVisitPageCountWhenException){
                         //todo: 记录失败
                         //todo: 放回原有队列队尾？
                         return;
                     }
                     Thread.sleep(GlobalOption.visitSessionPageSleepTime);
                 }
                }
            }

            //从selenium driver获得的浏览器的当前url可能与浏览器的实际url不一致
            //当浏览器从上一个网页直接访问下一个网页时，从selenium driver获得的浏览器的当前url可能是上一个网页
            //所以需要对这种情况进行特殊处理
            //处理方式为，访问完一个网页后，检查从driver获得的当前url和访问的目标url是否相等
            // 如果不相等，则休眠visitSessionPageSleepTime时间后再次检查
            //直到相等或者超过最大次数限制
            //如果超过最大次数还不一致，有两种情况
            //1. 页面有跳转
            //2. driver获得的url仍然和浏览器的当前url不一致
            this.sleepForPageReady(url);
//            int sleepCount = 0;
//            while(!this.browser.getUrl().toLowerCase().equals(lowerUrl))
//            {
//                //System.out.println("--------------------------------url不一致，当前为" + this.browser.getUrl().toLowerCase());
//                //System.out.println("--------------------------------url不一致，应该为" + urlInfo.url.toString().toLowerCase());
//                Thread.sleep(GlobalOption.visitSessionPageSleepTime);
//                if(++sleepCount > GlobalOption.maxVisitSessionPageSleepCount){
//                    System.out.println("--------------------------------url不一致，超过最大等待次数。");
//                    System.out.println("--------------------------------url不一致，当前为" + this.browser.getUrl().toLowerCase());
//                    System.out.println("--------------------------------url不一致，应该为" + url);
//                    break;
//                }
//            }
            //检查页面变化是否是当前的pageHandler能处理的？（检查页面是否发生跳转）
            //todo:这里需要仔细考虑一下，是否改变UrlInfo定义（parentUrl的类型改为UrlInfo，以解决跳转，404等问题？
            UrlInfo cui = new UrlInfo(new URL(this.browser.getUrl()), null, urlInfo.data, urlInfo.handlerName);
            while(!pageHandler.isMatch(cui))
            {
                //进入此处，表示
                //1. 页面发生跳转 或者
                //2. driver获得的url和浏览器的当前url不一致
                //while循环用于处理这两种情况。
                //假设连续跳转，那么需要计数当超过最大允许次数后退出循环。
                if(count > BrowserController.maxAllowedJumpNumber)
                {
                    logger.error("page jump count exceeds allowed limit: " + BrowserController.maxAllowedJumpNumber + ". break to continue with original page handler.");
                    break;//return其实也可以
                }
                logger.info("可能发生跳转,从[" + url + "]到[" + cui.url + "].");
                visited = true;
                //找到新的page处理者
                IPageHandler ph = this.findPageHandler(urlInfo);
                //check 以避免ph1和pageHandler指向同一个对象，造成的死循环

                if(ph == null)
                {
                    logger.info("no page handler for url: " + urlInfo.url.toString());
                    logger.info("visit next url.");
                    //不退出，试着让pageHandler接着处理。
                    break;
                }
                if( ph == pageHandler){
                    break;
                }
                logger.info("找到新的页面处理者: " + ph);
//                    this.rm.error("找到新的处理者[" + ph1); 统一日志处理方案
                //使用新的page处理者进行处理。其实还有一种处理方式是把新的页加入队列的头，重新访问
                this.handlePage(ph, cui, true, ++count);
                this.sleepForPageReady(cui.url.toString());
                //如果driver获得当前url和访问url一致，说明已经访问过了，不需要在跳转了，直接返回。
                if(this.browser.getUrl().toLowerCase().equals(cui.url.toString().toLowerCase())){
                    return;
                }
                else {
                    //不一致，那么有可能跳转，则继续循环
                    cui = new UrlInfo(new URL(this.browser.getUrl()), "");
                }
            }

            //执行对应的
            pageHandler.afterLoadPage(this.browser);
            //获得数据对象
            this.savePageData(pageHandler, urlInfo);

            this.savePublicUrls(pageHandler);

            this.savePrivateUrls(pageHandler);

            //写入redis库
            if (GlobalOption.useRedisToReduceDuplication )
            {
                UrlCache uc= new UrlCache(urlInfo,"");
                RedisUrlCacheManager.set(uc);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run()
    {
        UrlInfo firsturlInfo = null;
        try {
            if(this.sessionHandler != null)
            {
                this.sessionHandler.beforeStartSession(this.browser);
                //休息并更新可能的url变化，这里还需要重新考虑
                Thread.sleep(2000);
                if(browser.getUrl().startsWith("http")) {
                    firsturlInfo = sessionUrlQueue.poll();
                    try {
                        firsturlInfo.url = new URL(browser.getUrl());
                        sessionUrlQueue.putFirst(firsturlInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            logger.error("error occurs before starting session: ", ex);
        }
        BrowseUrlQueue<UrlInfo> savedQueue = null;
        if(GlobalOption.isCircleTask && firsturlInfo == null){
            //保存整个队列
            try {
                savedQueue = sessionUrlQueue.cloneQueue();
            }
            catch (Exception ex){
                //todo：保存队列失败，重复性任务将不再执行。
                GlobalOption.isCircleTask = false;
            }
        }
        while(true) {
            BrowserManager.suspendRunning();
            if(BrowserManager.exitMainThreadFlag){ //简单退出，不保存未完成的任务
                return;
            }
            while (sessionUrlQueue.size() > 0) {
                //用户可能要求暂停任务执行，就等待用户唤醒
                BrowserManager.suspendRunning();
                if(BrowserManager.exitMainThreadFlag){//简单退出，不保存未完成的任务
                    return;
                }
                UrlInfo urlInfo = sessionUrlQueue.poll();
                System.out.println("before visit " + urlInfo.url.toString());
                IPageHandler pageHandler = this.findPageHandler(urlInfo);
                if (pageHandler == null) {
                    logger.error("no page handler to deal with url: " + urlInfo.url.toString());
                    logger.info("visit next url.");
                    continue;
                }
                if (GlobalOption.useRedisToReduceDuplication) {
                    try {
                        if (RedisUrlCacheManager.exist(urlInfo.url.toString())) {
                            logger.info("url visited before, exists in redis: " + urlInfo.url.toString());
                            UrlCache uc = RedisUrlCacheManager.get(urlInfo.url.toString());
                            if (pageHandler.needVisit(uc) == false) {
                                logger.info("no need to visit the url again: " + urlInfo.url.toString());
                                logger.info("visit next.");
                                continue;
                            }
                        }
                    } catch (Exception ex) {
                        logger.error("redis error: " + ex.getMessage());
                        continue;
                    }
                }
                this.handlePage(pageHandler, urlInfo, false, 1);
                try {
                    Thread.sleep(GlobalOption.waitTimeToVisitNextSessionUrl);
                }catch (Exception ex){
                    logger.error("thread sleep error: " + ex.getMessage());
                }
            }
            if(GlobalOption.isCircleTask) {
                try {
                    sessionUrlQueue = savedQueue.cloneQueue();
                    Thread.sleep(GlobalOption.circleTaskSleepSeconds * 1000);
                    GlobalOption.startCircleNow = true;
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            else{
                break;
            }
        }
        try {
            if(this.sessionHandler != null) {
                //
                this.sessionHandler.afterEndSession(this.browser);
                // 并保存数据对象
                this.sessionHandler.saveSessionData(this.browser);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            //System.out.println("error: " + ex.getMessage());
        }
        finally {
            //or return web driver to pool
            this.browser.quit();
            BrowserManager.browserControllers.remove(this);
        }
    }
    private void savePublicUrls(IPageHandler pageHandler)
    {
        if(pageHandler != null) {
            //获得所有公共队列的链接
            List<List<UrlInfo>> seedUrls = pageHandler.getPublicLinks(this.browser);
            //todo: 将l_urls加入加入公共队列（去重）
            if (seedUrls != null) {
                for (List<UrlInfo> u : seedUrls) {
                    if(u!=null) {
                        try {
                            BrowserManager.insertUrlInfo(u);
                        } catch (Exception iex) {
                            logger.error("插入公共队列失败：" + u.toString());
                        }
                    }
                    else{
                        logger.warn("队列元素为空。");
                    }
                }
            }
        }
    }
    private void savePrivateUrls(IPageHandler pageHandler)
    {
        if(pageHandler != null) {
            //获得私有队列的链接
            List<UrlInfo> sessionUrls = pageHandler.getPrivateLinks(this.browser);
            //todo: 将urls加入私有队列
            if (sessionUrls != null) {
                for (UrlInfo l : sessionUrls) {
                    try {
                        //将PrivateUrls放入队尾
//                        this.sessionUrlQueue.put(l);
                        //将PrivateUrls放入队首
//                        this.sessionUrlQueue.putFirst(l);
                        if(l.title == null && l.time == null) {
                            //将list Urls放入队首
                            this.sessionUrlQueue.putFirst(l);
                        } else {
                            //将article Urls放入队首
                            this.sessionUrlQueue.put(l);
                        }
                    } catch (InterruptedException iex) {
                        logger.error("插入私有队列失败：" + l.toString());
                    }
                }
            }
        }
    }

    protected void savePageData(IPageHandler pageHandler, UrlInfo urlInfo){
        //执行用户自定义方法，提取页面数据并保存在urlInfo.data中。如果需要框架统一来保存，则返回urlInfo.data。有可能用户已经进行了存储。
        //如果用户不使用自定义方法，则不要改写父类方法
        boolean needSave = pageHandler.savePageData(this.browser);

        if(needSave && this.dataSaver != null) {
            try {
                this.dataSaver.saveData(urlInfo);
            } catch (IOException ioe) {
                //todo: notify
                ioe.printStackTrace();
            }
            catch (Exception ex) {
                //todo: notify
                ex.printStackTrace();
            }
        }
    }
    public boolean isMatch(UrlInfo ui)
    {
        return false;
    }

    public BrowserController cloneInstance(UrlInfo urlInfo)
            throws InterruptedException
    {
        return new BrowserController(urlInfo);
    }
}
