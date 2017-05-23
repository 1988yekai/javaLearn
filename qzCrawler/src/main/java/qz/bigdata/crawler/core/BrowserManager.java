package qz.bigdata.crawler.core;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.configuration.GlobalOption;
import qz.bigdata.crawler.monitor.*;
import qz.bigdata.crawler.store.ISaveData;
import qz.bigdata.crawler.store.hdfs.HdfsOptImp;
import qz.bigdata.crawler.store.mysql.MysqlForCrawler;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by fys on 2015/1/11.
 */
public class BrowserManager implements IBrowserManager {
    public String project;
    public String module;
    public String version;
    public static boolean exitMainThreadFlag = false;
    public static boolean suspendAllFlag = false;
    public static Lock lock = new ReentrantLock();
    private static Condition waitCondition = lock.newCondition();
    private static Condition endCondition = lock.newCondition();
    public static boolean suspendNowFlag = false;
    private ISaveData dataSaver;
    private HashMap<String, BrowserController> registeredBrowserControllers;
    private RedisCrawlerUtil crawlerUtil;
    private CrawlerNode crawlerNode;
    private static RedisUrlInfoQueue redisUrlInfoQueue;
    //种子队列列表
    private static BrowseUrlQueue<List<UrlInfo>> seedUrlsQueue = new BrowseUrlQueue<List<UrlInfo>>();
    //BrowserController列表
    public static List<BrowserController> browserControllers = new LinkedList<BrowserController>();
//    private Date startTime;
//    private Date endTime;
//    private TimeUnit sleepTime;
    private Logger logger = Logger.getLogger(getClass());
    public BrowserManager(String configFile)
    {
        this.registeredBrowserControllers = new HashMap<String, BrowserController>();
        this.init(configFile);
        //this.urls = new BrowseUrlQueue();
    }
    public void setCrawlerInfo(String project, String module, String version){
        this.project = project;
        this.module = module;
        this.version = version;
    }
    //用户自定义的存储
    public void setDataSaver(ISaveData dataSaver){
        this.dataSaver = dataSaver;
    }
    protected void init(String crawlerName)
    {
        try {
            if(GlobalOption.saveData) {
                if (GlobalOption.saveType == SaveType.none) {
                    this.dataSaver = null;
                }
                else if (GlobalOption.saveType == SaveType.hdfs) {
                    this.dataSaver = HdfsOptImp.getInstance(crawlerName);
                }
                else if(GlobalOption.saveType == SaveType.mysql){
                    //this.dataSaver =;
                    MysqlForCrawler datasql = new MysqlForCrawler(crawlerName);
                    this.dataSaver = datasql;
                    datasql.createPageDataTable();
                }
            }
            if(GlobalOption.recordCrawlerStatus){
                this.crawlerUtil = new RedisCrawlerUtil();
                //todo: init it from config file.
                this.crawlerNode = new CrawlerNode();
                crawlerNode.project = new CrawlerProject("test", "1.0");
            }
            if(GlobalOption.enableRedisQueue){
                this.redisUrlInfoQueue = new RedisUrlInfoQueue();
            }
        }
        catch (Exception e){
            //todo: notify administrator to deal
        }
    }
    public static BrowseUrlQueue getBrowseUrlQueue()
    {
        return BrowserManager.seedUrlsQueue;
    }
    public static void insertUrlInfo(List<UrlInfo>[] lists)
            throws Exception
    {
        for(List<UrlInfo> cell : lists) {
            if(GlobalOption.enableRedisQueue){
                BrowserManager.redisUrlInfoQueue.push(cell);
            }
            else {
                BrowserManager.seedUrlsQueue.put(cell);
            }
        }
    }
    public static void insertUrlInfo(List<UrlInfo> cell)
            throws Exception
    {
        if(GlobalOption.enableRedisQueue){
            BrowserManager.redisUrlInfoQueue.push(cell);
        }
        else {
            BrowserManager.seedUrlsQueue.put(cell);
        }
    }
    public static void insertUrlInfo(UrlInfo url)
            throws Exception
    {
        List<UrlInfo> cell = new LinkedList<UrlInfo>();
        cell.add(url);
        if(GlobalOption.enableRedisQueue){
            BrowserManager.redisUrlInfoQueue.push(cell);
        }
        else {
            BrowserManager.seedUrlsQueue.put(cell);
        }
    }
    public Boolean registerBrowserController(BrowserController bc)
            throws Exception
    {
        if(this.registeredBrowserControllers.containsKey(bc.getName()) == false)
        {
            this.registeredBrowserControllers.put(bc.getName(), bc);
            return true;
        }
        else
        {
            throw new Exception("已包含同样关键字的项目。插入项目失败。name：" + bc.getName());
        }
    }
    public Boolean updateBrowserController(BrowserController bc)
            throws Exception
    {
        if(this.registeredBrowserControllers.containsKey(bc.getName()) ==  true)
        {
            this.registeredBrowserControllers.put(bc.getName(), bc);
            return true;
        }
        else
        {
            throw new Exception("未包含同样关键字的项目。升级项目失败。name：" + bc.getName());
        }
    }
    public Boolean unregisterBrowserController(String name)
            throws Exception
    {
        if(this.registeredBrowserControllers.containsKey(name) == true )
        {
            this.registeredBrowserControllers.remove(name);
            return true;
        }
        else
        {
            throw new Exception("未包含同样关键字的项目。删除项目失败。name：" + name);
        }
    }
    public int count()
    {
        return this.registeredBrowserControllers.size();
    }
    //当从UrlQueue取得一个新的Url时，调用匹配的IBrowserController，并调用它的start方法
    public void onUrlRequest(List<UrlInfo> uis)
    throws  Exception
    {
//        BrowserController browserController = this.findBrowserController(bu);

        BrowserController browserController = new BrowserController(uis);
        browserController.setDataSaver(this.dataSaver);

        Thread bcThread = new Thread((Runnable) browserController);
        //System.out.println("start browser to visit " + bu.url.toString());
        bcThread.start();
        BrowserManager.browserControllers.add(browserController);
    }
    public BrowserController findBrowserController(UrlInfo ui)
            throws InterruptedException
    {
        for(BrowserController bc : this.registeredBrowserControllers.values())
        {
            if(bc.isMatch(ui))
            {
                return bc.cloneInstance(ui);
            }
        }
        logger.debug("not find browser controller, use default.");
        return new BrowserController(ui);
    }
    private BrowserController find(String url)
    {
        for(String urlRegex : this.registeredBrowserControllers.keySet())
        {
            if(this.map(urlRegex, url))
            {
                return this.registeredBrowserControllers.get(urlRegex);
            }
        }
        return null;
    }
    private Boolean map(String urlRegex, String url)
    {
        if(url.indexOf(urlRegex) >= 0) {
            System.out.println(url + " macthed " + urlRegex);
            return true;
        }
        //todo:
        System.out.println(url + " not macthed " + urlRegex);
        return false;
    }

    public void start()
    {
        recordBeforeStart();

        //GlobalOption.maxWaitForNewSeedUrlCount为0时，表示不用这个判断browser是否可能出现问题，所以会一直while
        boolean  alwaysWait = GlobalOption.maxWaitForNewSeedUrlCount == 0?true:false;

        //是否一直循环等待
        //不是的话，要判断等待次数是否超过最大允许等待次数，避免浏览器出问题造成的一直等待
        while( alwaysWait || Statistics.waitNewSeedUrlCount < GlobalOption.maxWaitForNewSeedUrlCount)
        {
            while(GlobalOption.maxAllowedParallelBrowserLimit != 0 &&
                    BrowserManager.browserControllers.size() >= GlobalOption.maxAllowedParallelBrowserLimit) {
                try {
                    Thread.sleep(GlobalOption.waitForAvailableBrowserSleepSeconds * 1000);
                }
                catch (Exception ex){
                    ex.printStackTrace();
                    return;
                }
            }
            List<UrlInfo> browseURL = null;
            if(!BrowserManager.suspendAllFlag) {
                continueNow();//唤醒其他等待的线程

                if (GlobalOption.enableRedisQueue) {
                    try {
                        browseURL = BrowserManager.redisUrlInfoQueue.pop();
                    }
                    catch (Exception ex){
                        continue;
                    }
                } else if (BrowserManager.getBrowseUrlQueue().size() > 0) {
                    browseURL = BrowserManager.seedUrlsQueue.poll();
                }
                if (browseURL != null) {
                    try {
                        this.onUrlRequest(browseURL);
                        //logger.info("Start visiting seed url " + browseURL.url.toString());
                        Statistics.visitedSeedUrlCount++;
                    } catch (Exception ex) {
                        //logger.error("error in visiting seed url: " + browseURL.url.toString(), ex);
                        ex.printStackTrace();
                        return;
                    }
                }
            }

            CrawlerStatus statusFlag = CrawlerStatus.Running;
            if(GlobalOption.recordCrawlerStatus){
                statusFlag = this.crawlerUtil.getCrawlerControlFlag(this.crawlerNode.project.name, this.crawlerNode.project.version);
                BrowserManager.exitMainThreadFlag = statusFlag == CrawlerStatus.Stopped ? true : false;
                BrowserManager.suspendAllFlag = statusFlag == CrawlerStatus.Suspending ? true : false;
            }
            //当检测到要求暂停任务执行指令时，此线程并不暂停，此线程负责持续监测指令是否变化（即从暂停到继续或退出），并通知其它工作线程。

            try {
                if(statusFlag != CrawlerStatus.Suspending){
                    continueNow(); //不是暂停，就通知其他线程继续
                }
                if(BrowserManager.exitMainThreadFlag)
                {
                    //todo:通知并等待其他线程退出
                    break;
                }
                if(BrowserManager.suspendAllFlag) //等待一阵时间，下一个循环再检测
                {
                    Thread.sleep(GlobalOption.waitTimeForNewSeedUrl);
                }
                //是否需要暂停任务运行？
                //BrowserManager.suspendRunning();
                if(browseURL == null || BrowserManager.seedUrlsQueue.size() <= 0)
                {
                    Thread.sleep(GlobalOption.waitTimeForNewSeedUrl);
                    Statistics.waitNewSeedUrlCount++;
                }
            }
            catch (Exception ex)
            {
                logger.error(ex);
                return;
            }
        }

        recordAfterEnd();
    }

    public static void suspendRunning(){
        lock.lock();
        try{
            if(suspendAllFlag) {
                waitCondition.await();
            }

        }catch (Exception ex){
        }
        finally {
            lock.unlock();
        }
    }
    public static void suspendNow(){
        suspendAllFlag = true;
    }
    public static void continueNow(){
        lock.lock();
        try{
            suspendAllFlag = false;
            waitCondition.signalAll();

        }catch (Exception ex){

        }
        finally {
            lock.unlock();
        }
    }
    private void recordBeforeStart()
    {
        System.out.println("start main thread of spider." + new Date().toString());
        System.out.println("start seed url count: " + BrowserManager.getBrowseUrlQueue().size());
        logger.info("start main thread of spider." + new Date().toString());

        if(GlobalOption.recordCrawlerStatus){
            this.crawlerUtil.crawlerStart(this.crawlerNode);
        }
    }
    private void recordAfterEnd()
    {
        System.out.println("total seed url count: " + Statistics.visitedSeedUrlCount);
        System.out.println("total wait seed url count: " + Statistics.waitNewSeedUrlCount);
        logger.info("end main thread of spider.");
        if(GlobalOption.recordCrawlerStatus){
            this.crawlerUtil.crawlerStop(this.crawlerNode);
        }
    }
}
