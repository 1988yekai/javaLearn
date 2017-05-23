package qz.bigdata.crawler.monitor;

/**
 * Created by fys on 2015/4/16.
 */
public interface ICrawlerRecorder {
    void crawlerStart(CrawlerNode instance);
    void crawlerSuspend(CrawlerNode instance);
    void crawlerResume(CrawlerNode instance);
    void crawlerStop(CrawlerNode instance);
}
