package qz.bigdata.crawler.monitor;

import org.apache.log4j.Logger;
import qz.bigdata.crawler.configuration.GlobalOption;
import qz.bigdata.crawler.core.UrlInfo;
import qz.bigdata.crawler.store.redis.JsonPooledRedisClient;
import qz.bigdata.crawler.store.redis.RedisDistributedObjectManager;
import qz.bigdata.crawler.store.redis.RedisOperation;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by fys on 2015/4/16.
 */
public class RedisCrawlerUtil implements ICrawlerRecorder, IStatusController, IStatusMonitor {

    private static final Logger logger = Logger.getLogger(RedisOperation.class);

    private RedisDistributedObjectManager rm = new RedisDistributedObjectManager("a", 123,"a");
    private static JedisPool pool = null;
    //    private static String  urlQueue = GlobalOption.redisUrlQueueString;
    private static String password = GlobalOption.redisPassword;
    private static String statusRecordPrefix = "crawler_status_";
    private String crawler_projects_key = "crawler_projects";
    private String crawler_projects_flags_key = "crawler_projects_flags";
    private String crawler_project_key_prefix = "crawler_project_";

    private String urlInfo_queue = "queue_urlinfo";

    private JsonPooledRedisClient client;

    public RedisCrawlerUtil(){
        //获得本机ip和进程id，还是生成一个uuid？
        this.client = JsonPooledRedisClient.getJsonPooledRedisClient(GlobalOption.redisIP,
                GlobalOption.redisPort,
                GlobalOption.redisPassword);

        this.rm = new RedisDistributedObjectManager(GlobalOption.redisIP,
                GlobalOption.redisPort,
                GlobalOption.redisPassword);
    }

    @Override
    public void crawlerStart(CrawlerNode crawler) {
        crawler.status = CrawlerStatus.Running;
        recordStatus(crawler);
    }

    @Override
    public void crawlerSuspend(CrawlerNode crawler) {
        crawler.status = CrawlerStatus.Suspending;
        recordStatus(crawler);
    }

    @Override
    public void crawlerResume(CrawlerNode crawler) {
        crawler.status = CrawlerStatus.Running;
        recordStatus(crawler);
    }

    @Override
    public void crawlerStop(CrawlerNode crawler) {
        crawler.status = CrawlerStatus.Stopped;
        recordStatus(crawler);
    }

    private void recordStatus(CrawlerNode crawler){
//        String key = this.crawler_project_key_prefix + crawler.project + "_" + crawler.pv;
//        String field = crawler.uuid;
//        return this.client.hset(key, field, crawler);
        rm.set(crawler);
    }

    @Override
    public boolean notifyToResume(String project, String version) {
        String key = this.crawler_projects_flags_key;
        String field = project + "_" + version;
        return this.client.hset(key, field, CrawlerStatus.Running);
    }

    @Override
    public boolean notifyToSuspend(String project, String version) {
        String key = this.crawler_projects_flags_key;
        String field = project + "_" + version;
        return this.client.hset(key, field, CrawlerStatus.Suspending);
    }

    @Override
    public boolean notifyToStop(String project, String version) {
        String key = this.crawler_projects_flags_key;
        String field = project + "_" + version;
        return this.client.hset(key, field, CrawlerStatus.Stopped);
    }

    @Override
    public CrawlerStatus getCrawlerStatus(String project, String version) {
        return null;
    }
    private String getProjectKey(String project, String version){
        return this.crawler_project_key_prefix + project + "_" + version;
    }

    @Override
    public List<CrawlerNode> getCrawlerNodes(String project, String version) {
        String key = this.crawler_project_key_prefix + project + "_" + version;
        Map<String, CrawlerNode> map = this.client.hgetAll(key, CrawlerNode.class);

        List<CrawlerNode> nodes = new ArrayList<CrawlerNode>();
        for(CrawlerNode cell : map.values()){
            nodes.add(cell);
        }
        return nodes;
    }

    @Override
    public List<CrawlerProject> getAllCrawlerProjects() {
        String key = this.crawler_projects_key;
        Map<String, CrawlerProject> map = this.client.hgetAll(key, CrawlerProject.class);

        List<CrawlerProject> projects = new ArrayList<CrawlerProject>();
        for(CrawlerProject cell : map.values()){
            projects.add(cell);
        }
        return projects;
    }

    @Override
    public CrawlerStatus getCrawlerControlFlag(String project, String version) {
        String key = this.crawler_projects_flags_key;
        String field = project + "_" + version;
        return (CrawlerStatus) this.client.hget(key, field, CrawlerStatus.class);
    }
}
