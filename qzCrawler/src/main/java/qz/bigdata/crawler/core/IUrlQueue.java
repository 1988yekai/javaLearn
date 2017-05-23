package qz.bigdata.crawler.core;

/**
 * Created by fys on 2015/3/21.
 */
public interface IUrlQueue {
    UrlInfo popUrlInfo();
    void pushUrlInfo(UrlInfo urlInfo);
}
