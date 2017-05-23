package qz.bigdata.crawler.monitor;

import java.util.List;

/**
 * Created by fys on 2015/3/18.
 */
public interface IStatusMonitor {
    //void spiderStart(String spiderName, Date startTime, List<List<UrlInfo>> startUrls, String ip);
    //void spiderExit(String spiderName, Date exitTime, String ip);
    //void spiderException(String spiderName, Date time, String exceptionDetails);

    CrawlerStatus getCrawlerStatus(String project, String version);

    //返回某个爬虫工程的所有实例信息
    List<CrawlerNode> getCrawlerNodes(String project, String version);
    //返回所有爬虫工程及其版本号
    List<CrawlerProject> getAllCrawlerProjects();

    CrawlerStatus getCrawlerControlFlag(String project, String version);
}
