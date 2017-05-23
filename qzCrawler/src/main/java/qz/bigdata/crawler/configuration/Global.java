package qz.bigdata.crawler.configuration;


/**
 * Created by Administrator on 14-4-10.
 */
public class Global extends Dynamic {

    public  static Global instance =null;
    public   int maxAllowedParallelBrowserLimit;
    public  int waitForAvailableBrowserSleepSeconds;
    public  String browserType ;
    public  String browserDriverPath;
    public  int pageLoadTimeout;
    public  int scriptTimeout;
    public  int implicitlyWaitTimeout;
    public  int visitSessionPageSleepTime;
    public  int maxVisitSessionPageSleepCount;
    public  int maxVisitPageCountWhenException;

    ///main thread
    public  int waitTimeForNewSeedUrl ;
    public  long maxWaitForNewSeedUrlCount ;

    public  boolean isCircleTask;
    public  boolean startCircleNow ;
    public  int circleTaskSleepSeconds;

    ///redis settings
    public  boolean useRedisToReduceDuplication;
    public  String redisIP;
    public  int redisPort ;
    public  String redisPassword;
    public  int redisDB ;
    public  String redisDbName ;

    ///保存数据设置
    public  boolean saveData;
    public  String saveType;

    ///hdfs settings
    public  boolean useHdfs;
    public  String hdfsIP ;
    public  int hdfsPort0;
    public  int sizeToWrite;

    //cloud settings
    public String cloudUsername;
    public String cloudPassword;
    public String cloudType;

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getBrowserDriverPath() {
        return browserDriverPath;
    }

    public void setBrowserDriverPath(String browserDriverPath) {
        this.browserDriverPath = browserDriverPath;
    }

    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(int pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public int getScriptTimeout() {
        return scriptTimeout;
    }

    public void setScriptTimeout(int scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }

    public int getImplicitlyWaitTimeout() {
        return implicitlyWaitTimeout;
    }

    public void setImplicitlyWaitTimeout(int implicitlyWaitTimeout) {
        this.implicitlyWaitTimeout = implicitlyWaitTimeout;
    }

    public int getVisitSessionPageSleepTime() {
        return visitSessionPageSleepTime;
    }

    public void setVisitSessionPageSleepTime(int visitSessionPageSleepTime) {
        this.visitSessionPageSleepTime = visitSessionPageSleepTime;
    }

    public int getMaxVisitSessionPageSleepCount() {
        return maxVisitSessionPageSleepCount;
    }

    public void setMaxVisitSessionPageSleepCount(int maxVisitSessionPageSleepCount) {
        this.maxVisitSessionPageSleepCount = maxVisitSessionPageSleepCount;
    }

    public int getMaxVisitPageCountWhenException() {
        return maxVisitPageCountWhenException;
    }

    public void setMaxVisitPageCountWhenException(int maxVisitPageCountWhenException) {
        this.maxVisitPageCountWhenException = maxVisitPageCountWhenException;
    }

    public int getWaitTimeForNewSeedUrl() {
        return waitTimeForNewSeedUrl;
    }

    public void setWaitTimeForNewSeedUrl(int waitTimeForNewSeedUrl) {
        this.waitTimeForNewSeedUrl = waitTimeForNewSeedUrl;
    }

    public long getMaxWaitForNewSeedUrlCount() {
        return maxWaitForNewSeedUrlCount;
    }

    public void setMaxWaitForNewSeedUrlCount(long maxWaitForNewSeedUrlCount) {
        this.maxWaitForNewSeedUrlCount = maxWaitForNewSeedUrlCount;
    }

    public boolean isCircleTask() {
        return isCircleTask;
    }

    public void setCircleTask(boolean isCircleTask) {
        this.isCircleTask = isCircleTask;
    }

    public boolean isStartCircleNow() {
        return startCircleNow;
    }

    public void setStartCircleNow(boolean startCircleNow) {
        this.startCircleNow = startCircleNow;
    }

    public int getCircleTaskSleepSeconds() {
        return circleTaskSleepSeconds;
    }

    public void setCircleTaskSleepSeconds(int circleTaskSleepSeconds) {
        this.circleTaskSleepSeconds = circleTaskSleepSeconds;
    }

    public boolean isUseRedisToReduceDuplication() {
        return useRedisToReduceDuplication;
    }

    public void setUseRedisToReduceDuplication(boolean useRedisToReduceDuplication) {
        this.useRedisToReduceDuplication = useRedisToReduceDuplication;
    }

    public String getRedisIP() {
        return redisIP;
    }

    public void setRedisIP(String redisIP) {
        this.redisIP = redisIP;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

    public int getRedisDB() {
        return redisDB;
    }

    public void setRedisDB(int redisDB) {
        this.redisDB = redisDB;
    }

    public String getRedisDbName() {
        return redisDbName;
    }

    public void setRedisDbName(String redisDbName) {
        this.redisDbName = redisDbName;
    }

    public boolean isSaveData() {
        return saveData;
    }

    public void setSaveData(boolean saveData) {
        this.saveData = saveData;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public boolean isUseHdfs() {
        return useHdfs;
    }

    public void setUseHdfs(boolean useHdfs) {
        this.useHdfs = useHdfs;
    }

    public String getHdfsIP() {
        return hdfsIP;
    }

    public void setHdfsIP(String hdfsIP) {
        this.hdfsIP = hdfsIP;
    }

    public int getHdfsPort0() {
        return hdfsPort0;
    }

    public void setHdfsPort0(int hdfsPort0) {
        this.hdfsPort0 = hdfsPort0;
    }

    public int getSizeToWrite() {
        return sizeToWrite;
    }

    public void setSizeToWrite(int sizeToWrite) {
        this.sizeToWrite = sizeToWrite;
    }

    public static void setInstance(Global instance) {
        Global.instance = instance;
    }

    private Global() {
        super();
    }
    public static Global getInstance(){
        if(instance == null)
            instance = new Global();
        return instance;
    }


    public int getMaxAllowedParallelBrowserLimit() {
        return maxAllowedParallelBrowserLimit;
    }

    public void setMaxAllowedParallelBrowserLimit(int maxAllowedParallelBrowserLimit) {
        this.maxAllowedParallelBrowserLimit = maxAllowedParallelBrowserLimit;
    }

    public int getWaitForAvailableBrowserSleepSeconds() {
        return waitForAvailableBrowserSleepSeconds;
    }

    public void setWaitForAvailableBrowserSleepSeconds(int waitForAvailableBrowserSleepSeconds) {
        this.waitForAvailableBrowserSleepSeconds = waitForAvailableBrowserSleepSeconds;
    }

    public String getCloudUsername() {
        return cloudUsername;
    }

    public void setCloudUsername(String cloudUsername) {
        this.cloudUsername = cloudUsername;
    }

    public String getCloudPassword() {
        return cloudPassword;
    }

    public void setCloudPassword(String cloudPassword) {
        this.cloudPassword = cloudPassword;
    }

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }
}
