package qz.bigdata.crawler.configuration;


import qz.bigdata.crawler.browser.BrowserType;
import qz.bigdata.crawler.core.SaveType;

/**
 * Created by Administrator on 2015-03-17.
 */
public class GlobalOption {
    static Global go;
    static{
        try {
            GlobalOptionParser gp = new GlobalOptionParser();
            go=gp.initGlobalOption("GlobalOption.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String basePath = "\\resources\\"; //added by fys
    //browser settings (浏览器设置）
    public static int maxAllowedParallelBrowserLimit =go.maxAllowedParallelBrowserLimit;
    public static int waitForAvailableBrowserSleepSeconds = go.waitForAvailableBrowserSleepSeconds;
    public static BrowserType browserType = BrowserType.valueOf(BrowserType.class, go.browserType);
    public static String browserDriverPath= go.browserDriverPath;
    public static int pageLoadTimeout = go.pageLoadTimeout;//seconds
    public static int scriptTimeout = go.scriptTimeout; //seconds
    public static int implicitlyWaitTimeout = go.implicitlyWaitTimeout; //seconds
    public static int visitSessionPageSleepTime = go.visitSessionPageSleepTime;//访问session中
    public static int maxVisitSessionPageSleepCount = go.maxVisitSessionPageSleepCount;//访问session中
    public static int maxVisitPageCountWhenException = go.maxVisitPageCountWhenException;//访问session中


    ///main thread
    public static int waitTimeToVisitNextSessionUrl = 500; //BrowserController访问下一个session url时休眠时间。
    public static int waitTimeForNewSeedUrl = go.waitTimeForNewSeedUrl; //BrowserManager中当没有新的seed url时等待时间 毫秒
    public static long maxWaitForNewSeedUrlCount = go.maxWaitForNewSeedUrlCount; //如果为0，表示永远等待， 如果不为零，表示最大等待次数

    public static boolean enableRedisQueue = false;

    public static boolean isCircleTask = go.isCircleTask; //是否是周期性任务
    public static boolean startCircleNow = go.startCircleNow; //是否开始只访问种子url的周期性任务
    public static int circleTaskSleepSeconds = go.circleTaskSleepSeconds; //周期性任务休眠时间

    //management settings
    public static boolean recordCrawlerStatus = false;
    public static boolean controlCrawlerStatus = false;

    ///redis settings
    public static boolean useRedisToReduceDuplication = go.useRedisToReduceDuplication;
    public static String redisIP = go.redisIP; //"192.168.13.60";//
    public static int redisPort = go.redisPort;//6479;//
    public static String redisPassword = go.redisPassword;//"qz123456";//
    public static String redisUrlQueueString = "url_queue";
    public static int redisDB = go.redisDB;
    public static String redisDbName = go.redisDbName;

    ///保存数据设置
    public static boolean saveData = go.saveData;
    public static SaveType saveType = SaveType.valueOf(SaveType.class, go.saveType);

    ///hdfs settings
    public static boolean useHDFS = go.useHdfs;
    public static String hdfsIP = go.hdfsIP;
    public static int hdfsPort = go.hdfsPort0;

    ///cloudcode settings
    public static String cloudUsername = go.cloudUsername;
    public static String cloudPassword = go.cloudPassword;
    public static String cloudType = go.cloudType;

}
