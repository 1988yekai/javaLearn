package qz.bigdata.crawler.store;

import qz.bigdata.crawler.core.UrlInfo;

/**
 * Created by fys on 2015/2/27.
 */
public interface ISaveData {
    void saveData(UrlInfo urlInfo) throws Exception;
}
